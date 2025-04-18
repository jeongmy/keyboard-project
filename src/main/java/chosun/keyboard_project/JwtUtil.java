package chosun.keyboard_project;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String secretKey = "your_secret_key"; // 비밀 키
    private long expirationTime = 1000 * 60 * 60; // 토큰 만료 시간 (1시간)

    // JWT 토큰 생성
    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 토큰이 만료되었는지 체크
    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }

    // 토큰 검증
    public boolean validateToken(String token, String username) {
        return (username.equals(getUsername(token)) && !isTokenExpired(token));
    }
}