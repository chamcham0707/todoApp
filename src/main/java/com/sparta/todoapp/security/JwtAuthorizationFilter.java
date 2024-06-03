package com.sparta.todoapp.security;

import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.exception.InvalidTokenException;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String access = req.getHeader(jwtUtil.AUTHORIZATION_HEADER);
        boolean isAccessToken = true;
        if (access == null) {
            isAccessToken = false;
        }

        String tokenValue = jwtUtil.getJwtFromHeader(req, isAccessToken);

        if (StringUtils.hasText(tokenValue)) {

            checkValidateTokenAndErrorHandling(res, tokenValue, isAccessToken);

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue, isAccessToken);

            if (!isAccessToken) {
                sendNewAccessToken(res, info);
                return;
            }

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }


    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void checkValidateTokenAndErrorHandling(HttpServletResponse res, String tokenValue, boolean isAccessToken) throws IOException {
        switch (jwtUtil.validateToken(tokenValue, isAccessToken)) {
            case EXPIRE:
                if (isAccessToken) {
                    // client로 refresh Token을 요청하는 패킷 보내기
                    res.setStatus(401);
                    res.setContentType("text/plain;charset=UTF-8");
                    res.getWriter().write("Access Token이 만료되었습니다. Refresh Token을 보내 갱신하세요.");
                } else {
                    // 로그아웃 시킨다.
                }
            case INVALID_SIGNATURE:
            case UNSSPORT:
            case EMPTY:
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("text/plain;charset=UTF-8");
                res.getWriter().write("토큰이 유효하지 않습니다.");
                return;
            case NOT_ERROR:
                break;
        }
    }

    private void sendNewAccessToken(HttpServletResponse res, Claims info) throws IOException {
        UserRoleEnum role = userRepository.findByUsername(info.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자는 없습니다.")
        ).getRole();
        String accessToken = jwtUtil.createAccessToken(info.getSubject(), role);
        res.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        res.getWriter().write("new access token");
    }
}