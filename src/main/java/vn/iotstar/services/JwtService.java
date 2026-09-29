package vn.iotstar.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;


    // Lấy username (email) từ token
    public String extractUsername(String token) {

        return extractClaim(
            token,
            Claims::getSubject
        );
    }


    // Lấy một claim bất kỳ từ token
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        final Claims claims =
                extractAllClaims(token);

        return claimsResolver.apply(claims);
    }


    // Tạo token
    public String generateToken(
            UserDetails userDetails) {

        return generateToken(
            new HashMap<>(),
            userDetails
        );
    }


    // Tạo token có thêm extra claims
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(
                    new Date(System.currentTimeMillis())
                )
                .expiration(
                    new Date(
                        System.currentTimeMillis()
                        + jwtExpiration
                    )
                )
                .signWith(getSignInKey())
                .compact();
    }


    // Lấy thời gian hết hạn
    public long getExpirationTime() {

        return jwtExpiration;
    }


    // Kiểm tra token hợp lệ
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        final String username =
                extractUsername(token);

        return username.equals(
                    userDetails.getUsername()
                )
                && !isTokenExpired(token);
    }


    // Kiểm tra token hết hạn chưa
    private boolean isTokenExpired(
            String token) {

        return extractExpiration(token)
                .before(new Date());
    }


    // Lấy ngày hết hạn
    private Date extractExpiration(
            String token) {

        return extractClaim(
            token,
            Claims::getExpiration
        );
    }


    // Đọc toàn bộ Claims
    private Claims extractAllClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // Tạo SecretKey từ secret key
    private SecretKey getSignInKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(
                    secretKey
                );

        return Keys.hmacShaKeyFor(
            keyBytes
        );
    }
}