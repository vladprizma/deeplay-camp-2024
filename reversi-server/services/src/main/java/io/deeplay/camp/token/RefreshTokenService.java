package io.deeplay.camp.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.camp.entity.TokensRequest;
import io.deeplay.camp.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service for handling refresh token operations.
 * <p>
 * This class provides methods for generating, validating, and extracting information from refresh tokens.
 * It communicates with the token microservice using HTTP requests.
 * </p>
 */
public class RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
    private static final String BASE_URL = "http://localhost:8083/api/tokens"; 
    private static final String SECRET_KEY = "e6620c7252a94eb85d59399f3a07cc66c10f700365532dacf5dc234";
    private final Integer ONE_DAY = 1000 * 60 * 60 * 24;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generates a refresh token and an access token for the given user.
     * <p>
     * This method generates a refresh token and an access token, saves them in the microservice, and returns them
     * as a TokensRequest object.
     * </p>
     *
     * @param user The user for whom the tokens are generated.
     * @return The generated tokens.
     * @throws IOException If an IO error occurs during token saving.
     */
    public TokensRequest generateRefreshToken(User user) throws IOException {
        logger.info("Generating refresh token for user: {}", user.getUsername());
        String refreshToken = generateToken(new HashMap<>(), user);
        String updateToken = generateAccessToken(user);
        TokensRequest tokensRequest = new TokensRequest(refreshToken, updateToken, user.getId());

        String json = objectMapper.writeValueAsString(tokensRequest);
        sendRequest(BASE_URL, "POST", json);

        return tokensRequest;
    }

    /**
     * Validates the given refresh token against the stored token for the user.
     * <p>
     * This method checks if the given refresh token matches the stored token and if it is not expired.
     * </p>
     *
     * @param token The refresh token to be validated.
     * @param user  The user for whom the token is validated.
     * @return True if the token is valid, false otherwise.
     * @throws IOException If an IO error occurs during token retrieval.
     */
    public boolean validateRefreshToken(String token, User user) throws IOException {
        logger.info("Validating refresh token for user: {}", user.getUsername());
        String response = sendRequest(BASE_URL + "/validate", "POST", objectMapper.writeValueAsString(Map.of("token", token, "userId", user.getId())));
        return Boolean.parseBoolean(response) && !isTokenExpired(token);
    }

    /**
     * Generates an access token for the given user.
     * <p>
     * This method generates a new access token using the JwtService.
     * </p>
     *
     * @param user The user for whom the access token is generated.
     * @return The generated access token.
     */
    public String generateAccessToken(User user) {
        logger.info("Generating access token for user: {}", user.getUsername());
        JwtService jwtService = new JwtService();
        return jwtService.generateToken(user);
    }

    /**
     * Generates a token with additional claims for the given user.
     * <p>
     * This method generates a token with the specified claims and a one-day expiration time.
     * </p>
     *
     * @param extraClaims Additional claims to be included in the token.
     * @param user        The user for whom the token is generated.
     * @return The generated token.
     */
    private String generateToken(Map<String, Object> extraClaims, User user) {
        logger.info("Generating token with extra claims for user: {}", user.getUsername());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ONE_DAY))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if the given token is expired.
     * <p>
     * This method checks if the token's expiration date is before the current date.
     * </p>
     *
     * @param token The token to be checked.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        logger.info("Checking if token is expired");
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given token.
     * <p>
     * This method extracts the expiration date claim from the token.
     * </p>
     *
     * @param token The token from which the expiration date is extracted.
     * @return The expiration date.
     */
    private Date extractExpiration(String token) {
        logger.info("Extracting expiration date from token");
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the given token.
     * <p>
     * This method extracts the specified claim from the token using the provided claims resolver function.
     * </p>
     *
     * @param token          The token from which the claim is extracted.
     * @param claimsResolver The function to resolve the claim.
     * @param <T>            The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        logger.info("Extracting claim from token");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given token.
     * <p>
     * This method extracts all claims from the token.
     * </p>
     *
     * @param token The token from which the claims are extracted.
     * @return The extracted claims.
     */
    private Claims extractAllClaims(String token) {
        logger.info("Extracting all claims from token");
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key for token operations.
     * <p>
     * This method decodes the secret key and returns it as a Key object.
     * </p>
     *
     * @return The signing key.
     */
    private Key getSignInKey() {
        logger.info("Retrieving signing key");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String sendRequest(String urlString, String method, String json) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        if (json != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to execute request. HTTP response code: " + responseCode);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
