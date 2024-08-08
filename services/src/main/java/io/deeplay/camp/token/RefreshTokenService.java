package io.deeplay.camp.token;

import entity.User;
import io.deeplay.camp.dao.TokenDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Service for handling refresh token operations.
 * <p>
 * This class provides methods for generating, validating, and extracting information from refresh tokens.
 * It uses a secret key for signing the tokens and ensures the tokens are valid and not expired.
 * </p>
 */
public class RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
    private static final String SECRET_KEY = "e6620c7252a94eb85d59399f3a07cc66c10f700365532dacf5dc234";
    private final Integer ONE_DAY = 1000 * 60 * 60 * 24;
    private final TokenDAO tokenDAO = new TokenDAO();

    /**
     * Generates a refresh token and an access token for the given user.
     * <p>
     * This method generates a refresh token and an access token, saves them in the database, and returns them
     * as a TokensRequest object.
     * </p>
     *
     * @param user The user for whom the tokens are generated.
     * @return The generated tokens.
     * @throws SQLException If a SQL error occurs during token saving.
     */
    public TokensRequest generateRefreshToken(User user) throws SQLException {
        logger.info("Generating refresh token for user: {}", user.getUsername());
        String refreshToken = generateToken(new HashMap<>(), user);
        String updateToken = generateAccessToken(user);
        var tokensRequest = new TokensRequest(refreshToken, updateToken);
        tokenDAO.saveToken(user.getId(), refreshToken, updateToken);
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
     * @throws SQLException If a SQL error occurs during token retrieval.
     */
    public boolean validateRefreshToken(String token, User user) throws SQLException {
        logger.info("Validating refresh token for user: {}", user.getUsername());
        final Optional<String> storedToken = tokenDAO.getRefreshToken(user.getId());
        return storedToken.isPresent() && storedToken.get().equals(token) && !isTokenExpired(token);
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
}