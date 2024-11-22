package ru.kpfu.itis.paramonov.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.kpfu.itis.paramonov.dto.responses.ErrorResponseDto;
import ru.kpfu.itis.paramonov.entity.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import ru.kpfu.itis.paramonov.entity.Token;
import ru.kpfu.itis.paramonov.repository.TokenRepository;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer";

    private final JwtProvider jwtProvider;

    private final TokenRepository tokenRepository;

    public static JwtAuthentication generate(Claims claims) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setRoles(getRoles(claims));
        jwtAuthentication.setUsername(claims.getSubject());
        jwtAuthentication.setId(claims.get("id", Long.class));
        return jwtAuthentication;
    }

    private static Set<Role> getRoles(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        return roles
                .stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null) {
            if (jwtProvider.validateToken(token)) {
                Claims claims = jwtProvider.getClaims(token);
                JwtAuthentication jwtAuthentication = generate(claims);
                Token dbToken = tokenRepository.getTokenByUserIdAndToken(
                        jwtAuthentication.getId(), token
                );
                if (dbToken.isInvalidated()) {
                    var httpServletResponse = (HttpServletResponse) servletResponse;
                    httpServletResponse.setContentType("application/json");
                    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    ErrorResponseDto responseDto = new ErrorResponseDto(
                            HttpStatus.UNAUTHORIZED.value(), "Token is invalidated"
                    );
                    new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), responseDto);
                } else {
                    jwtAuthentication.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (bearer != null && bearer.startsWith(BEARER)) {
            return bearer.substring(BEARER.length()).trim();
        }
        return null;
    }
}