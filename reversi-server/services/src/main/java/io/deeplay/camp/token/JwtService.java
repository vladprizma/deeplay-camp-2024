package io.deeplay.camp.token;

import io.deeplay.camp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for handling JWT operations.
 * <p>
 * This class provides methods for generating, validating, and extracting information from JWT tokens.
 * It uses a secret key for signing the tokens and ensures the tokens are valid and not expired.
 * </p>
 */
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final String SECRET_KEY = "e6620c7252a94eb85d59399f3a07cc66c10f700365532dacf5dc234b1edf3c7c";
    private final Integer HALF_AN_HOUR = 1000 * 60 * 30 * 3000;

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        logger.info("Extracting username from token");
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(User user) {
        logger.info("Generating token for user: {}", user.getUsername());
        return generateToken(new HashMap<>(), user);
    }

    /**
     * Generates a JWT token with additional claims for the given user.
     *
     * @param extraClaims Additional claims to be included in the token.
     * @param user        The user for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, User user) {
        logger.info("Generating token with extra claims for user: {}", user.getUsername());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + HALF_AN_HOUR))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the given JWT token against the user details.
     *
     * @param token      The JWT token to be validated.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, User userDetails) {
        logger.info("Validating token for user: {}", userDetails.getUsername());
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token The JWT token to be checked.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        logger.info("Checking if token is expired");
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date extracted from the token.
     */
    private Date extractExpiration(String token) {
        logger.info("Extracting expiration date from token");
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the given JWT token.
     *
     * @param token          The JWT token.
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
     * Extracts all claims from the given JWT token.
     *
     * @param token The JWT token.
     * @return The claims extracted from the token.
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
     * Retrieves the signing key for JWT operations.
     *
     * @return The signing key.
     */
    private Key getSignInKey() {
        logger.info("Retrieving signing key");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}