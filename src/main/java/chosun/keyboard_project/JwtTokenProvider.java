package chosun.keyboard_project;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {


    @Value("${jwt.secret}")
    private String secretKey;

    private final long expiration = 1000L * 60 * 60; // 1시간

    @PostConstruct // Spring이 이 Bean을 초기화할 때 자동으로 실행됨
    protected void init() {
        // 비밀 키(secretKey)를 Base64 인코딩
        // → JWT 서명을 위해 키를 인코딩된 문자열로 바꿔줌 (라이브러리가 이 형식을 기대함)
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);

        Date now = new Date(); // 현재 시간
        Date expiry = new Date(now.getTime() + expiration); // 만료 시간

        return Jwts.builder() // 	JWT를 조립 (페이로드, 발급 시간, 만료 시간, 서명 등)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey) // 	비밀 키로 서명. HMAC SHA-256 사용
                .compact(); // 최종 JWT 문자열 반환
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
