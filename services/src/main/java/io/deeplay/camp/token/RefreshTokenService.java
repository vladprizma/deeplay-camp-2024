package io.deeplay.camp.token;

import entity.User;
import io.deeplay.camp.dao.TokenDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class RefreshTokenService {
    private static final String SECRET_KEY = "e6620c7252a94eb85d59399f3a07cc66c10f700365532dacf5dc234";
    private final Integer ONE_DAY = 1000 * 60 * 60;
    private final TokenDAO tokenDAO = new TokenDAO();

    public TokensRequest generateRefreshToken(User user) throws SQLException {
        String refreshToken = generateToken(new HashMap<>(), user);
        String updateToken = generateAccessToken(user);
        var tokensRequest = new TokensRequest(refreshToken, updateToken);
        tokenDAO.saveToken(user.getId(), refreshToken, updateToken);
        return tokensRequest;
    }

    public boolean validateRefreshToken(String token, User user) throws SQLException {
        final Optional<String> storedToken = tokenDAO.getRefreshToken(user.getId());
        return storedToken.isPresent() && storedToken.get().equals(token) && !isTokenExpired(token);
    }

    public String generateAccessToken(User user) {
        JwtService jwtService = new JwtService();
        return jwtService.generateToken(user);
    }

    private String generateToken(Map<String, Object> extraClaims, User user) {
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