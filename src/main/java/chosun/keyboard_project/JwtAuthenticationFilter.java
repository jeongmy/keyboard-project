package chosun.keyboard_project;

import chosun.keyboard_project.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//[요청 도착]
// → Spring Security FilterChain 실행
//   → JwtAuthenticationFilter가 순서상 도착
//     → JwtAuthenticationFilter.doFilter() 자동 호출됨
//       → 내부에서 doFilterInternal() 자동 호출됨 ← 작성한 핵심 로직 실행

// 항목	                                   설명
// doFilter()	            서블릿 필터의 진입점. Spring이 자동으로 호출
// doFilterInternal()	    내가 오버라이딩해서 JWT 인증 로직 작성하는 곳
// 왜 자동 실행?	            OncePerRequestFilter가 내부적으로 doFilterInternal()을 호출하도록 구현돼 있어서

// 사용자가 서버에 요청을 보낼 때마다 이 필터가 한 번만 실행되어 JWT 토큰을 검사하고, 유효하면 인증 정보를 설정
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequsetFilter: 요청당 한 번만 실행되는 Spring Security 필터
    // (한 요청에서 여러 서블릿이 실행돼도 필터는 한 번만 적용되게 보장함

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, // 자동 주입
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String token = resolveToken(request); // Authorization 헤더에서 JWT 토큰만 뽑아냄

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // 여기서 권한을 읽어서 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // 클라이언트가 보낸 요청 헤더에서 JWT 토큰을 추출하기 위한 함수
    private String resolveToken(HttpServletRequest request) {
        // HttpServletRequest request: 클라이언트의 HTTP 요청 정보를 담고 있는 객체.
        // Spring Web MVC에서는 컨트롤러, 필터, 인터셉터, 시큐리티 필터 등에서 이 객체를 자동으로 주입해줌
        // Spring Security 필터나 일반 필터에서 doFilterInternal 메서드가 실행될 때,
        // Spring이 request와 response를 자동으로 전달.

        String bearerToken = request.getHeader("Authorization");
        // 클라이언트 요청에서 Authorization 헤더 값을 가져옴.
        // ex) Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
        // bearerToken 변수에는 "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." 이 저장됨
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // StringUtils.hasText(...): 문자열이 null이 아니고, 빈 문자열("")이 아니고, 공백만 있는 문자열이 아닌지를 확인.
            return bearerToken.substring(7); // "Bearer " 이후 부분만 자름
        }
        return null;
    }
}