package com.sparta.todoapp.jwt;

import com.sparta.todoapp.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
//    private final long ACCESS_TOKEN_TIME = 1000L; // 1초
    private final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L; // 1일

    @Value("${jwt.access.secret.key}") // Base64 Encode 한 SecretKey
    private String accessSecretKey;

    @Value("${jwt.refresh.secret.key}") // Base64 Encode 한 SecretKey
    private String refreshSecretAKey;
    private Key accessKey;
    private Key refreshKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] accessBytes = Base64.getDecoder().decode(accessSecretKey);
        accessKey = Keys.hmacShaKeyFor(accessBytes);

        byte[] refreshBytes = Base64.getDecoder().decode(refreshSecretAKey);
        refreshKey = Keys.hmacShaKeyFor(refreshBytes);
    }

    // 토큰 생성
    public String createAccessToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(accessKey, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    public String createRefreshToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(refreshKey, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request, boolean isAccessToken) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (!isAccessToken) {
            bearerToken = request.getHeader(REFRESH_HEADER);
        }
        log.info("bearerToken: " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public TokenErrorEnum validateToken(String token, boolean isAccessToken) {
        Key key = accessKey;
        if (!isAccessToken) {
            key = refreshKey;
        }

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // jjwt를 사용하여 JWT 토큰을 파싱하고 검증하는 작업을 수행
            return TokenErrorEnum.NOT_ERROR;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            return TokenErrorEnum.INVALID_SIGNATURE;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            return TokenErrorEnum.EXPIRE;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            return TokenErrorEnum.UNSSPORT;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            return TokenErrorEnum.EMPTY;
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token, boolean isAccessToken) {
        Key key = accessKey;
        if (!isAccessToken) {
            key = refreshKey;
        }

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}