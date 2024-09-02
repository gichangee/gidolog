package com.gildong.api.config;

import com.gildong.api.config.data.UserSession;
import com.gildong.api.exception.Unauthorized;
import com.gildong.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    private final AppConfig appConfig;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info(">>>>>{}",appConfig.toString());
        String jws = webRequest.getHeader("Authorization");

        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        try {

            SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jws);

            log.info(">>>>>>>{}",claimsJws);

            Long userId = Long.parseLong(claimsJws.getPayload().getSubject());

            return new UserSession(userId);
        } catch (JwtException e) {
            throw new Unauthorized();
        }


    }
}
