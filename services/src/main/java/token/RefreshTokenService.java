package token;

import entity.Player;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class RefreshTokenService {
    private static final String SECRET_KEY = "e6620c7252a94eb85d59399f3a07cc66c10f700365532dacf5dc234b1edf3c7c";
    private final Integer ONE_DAY = 1000 * 60 * 60 * 24;
    private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    public String generateRefreshToken(Player user) {
        String refreshToken = generateToken(new HashMap<>(), user);
        refreshTokenStore.put(user.getUsername(), refreshToken);
        return refreshToken;
    }

    public boolean validateRefreshToken(String token, Player user) {
        final String storedToken = refreshTokenStore.get(user.getUsername());
        return storedToken != null && storedToken.equals(token) && !isTokenExpired(token);
    }

    public String generateAccessToken(Player user) {
        JwtService jwtService = new JwtService();
        return jwtService.generateToken(user);
    }

    private String generateToken(Map<String, Object> extraClaims, Player user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ONE_DAY))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
