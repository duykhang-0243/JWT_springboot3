package vn.iotstar.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;


    // =====================================================
    // LẤY USERNAME (EMAIL) TỪ TOKEN
    // =====================================================
    public String extractUsername(String token) {

        try {

            SignedJWT signedJWT =
                    SignedJWT.parse(token);

            JWTClaimsSet claims =
                    signedJWT.getJWTClaimsSet();

            return claims.getSubject();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Không thể đọc JWT",
                    e
            );
        }
    }


    // =====================================================
    // TẠO TOKEN
    // =====================================================
    public String generateToken(
            UserDetails userDetails) {

        return generateToken(
                new HashMap<>(),
                userDetails
        );
    }


    // =====================================================
    // TẠO TOKEN CÓ THÊM EXTRA CLAIMS
    // =====================================================
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {

        try {

            Date now =
                    new Date();

            Date expiration =
                    new Date(
                            System.currentTimeMillis()
                            + jwtExpiration
                    );


            // ---------------------------------------------
            // 1. TẠO PAYLOAD / CLAIMS
            // ---------------------------------------------
            JWTClaimsSet.Builder claimsBuilder =
                    new JWTClaimsSet.Builder()
                            .subject(
                                    userDetails.getUsername()
                            )
                            .issueTime(now)
                            .expirationTime(expiration);


            // Thêm các extra claims nếu có
            for (Map.Entry<String, Object> entry
                    : extraClaims.entrySet()) {

                claimsBuilder.claim(
                        entry.getKey(),
                        entry.getValue()
                );
            }


            JWTClaimsSet claimsSet =
                    claimsBuilder.build();


            // ---------------------------------------------
            // 2. TẠO HEADER
            // ---------------------------------------------
            JWSHeader header =
                    new JWSHeader(
                            JWSAlgorithm.HS256
                    );


            // ---------------------------------------------
            // 3. TẠO SIGNED JWT
            // ---------------------------------------------
            SignedJWT signedJWT =
                    new SignedJWT(
                            header,
                            claimsSet
                    );


            // ---------------------------------------------
            // 4. TẠO SIGNER BẰNG SECRET KEY
            // ---------------------------------------------
            MACSigner signer =
                    new MACSigner(
                            getSecretKeyBytes()
                    );


            // ---------------------------------------------
            // 5. KÝ JWT
            // ---------------------------------------------
            signedJWT.sign(signer);


            // ---------------------------------------------
            // 6. CHUYỂN JWT THÀNH CHUỖI
            // ---------------------------------------------
            return signedJWT.serialize();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Không thể tạo JWT",
                    e
            );
        }
    }


    // =====================================================
    // LẤY THỜI GIAN HẾT HẠN
    // =====================================================
    public long getExpirationTime() {

        return jwtExpiration;
    }


    // =====================================================
    // KIỂM TRA TOKEN HỢP LỆ
    // =====================================================
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        try {

            // Parse JWT
            SignedJWT signedJWT =
                    SignedJWT.parse(token);


            // Tạo verifier bằng cùng secret key
            MACVerifier verifier =
                    new MACVerifier(
                            getSecretKeyBytes()
                    );


            // Kiểm tra chữ ký
            boolean signatureValid =
                    signedJWT.verify(verifier);


            if (!signatureValid) {
                return false;
            }


            // Lấy Claims
            JWTClaimsSet claims =
                    signedJWT.getJWTClaimsSet();


            // Lấy username
            String username =
                    claims.getSubject();


            // Lấy expiration
            Date expiration =
                    claims.getExpirationTime();


            // Kiểm tra username
            boolean usernameValid =
                    username != null
                    && username.equals(
                            userDetails.getUsername()
                    );


            // Kiểm tra expiration
            boolean tokenNotExpired =
                    expiration != null
                    && expiration.after(
                            new Date()
                    );


            return usernameValid
                    && tokenNotExpired;

        } catch (Exception e) {

            return false;
        }
    }


    // =====================================================
    // CHUYỂN SECRET KEY THÀNH BYTE[]
    // =====================================================
    private byte[] getSecretKeyBytes() {

        return secretKey.getBytes(
                StandardCharsets.UTF_8
        );
    }
}