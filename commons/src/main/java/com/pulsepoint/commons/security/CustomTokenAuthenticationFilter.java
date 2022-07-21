package com.pulsepoint.commons.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

@Component
public class CustomTokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    TokenAuthenticator tokenAuthenticator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        AbstractAuthenticationToken userAuthenticationToken;

        if (isNonAuthenticationRequest(request)) {
            userAuthenticationToken = getUserAuthenticationToken("-1");
        } else {
            UserContextDTO userContextDTO = tokenAuthenticator.authenticate(request);
            userAuthenticationToken = getUserAuthenticationToken(userContextDTO);
        }
        setAuthenticationToSecurityContext(userAuthenticationToken);
        chain.doFilter(request, response);
    }

    private void setAuthenticationToSecurityContext(AbstractAuthenticationToken userAuthenticationToken) {
        SecurityContextHolder.getContext().setAuthentication(userAuthenticationToken);
    }

    private boolean isNonAuthenticationRequest(HttpServletRequest request) {
        String requestUri = request.getRequestURI().toLowerCase();
        return isOptionsRequest(request) || isNonResourceUrl(requestUri);
    }

    private boolean isNonResourceUrl(String requestUri) {
        return Stream.of("swagger", "favicon", "api-docs", "configuration/security", "configuration/ui").anyMatch(requestUri::contains);
    }

    private boolean isOptionsRequest(HttpServletRequest request) {
        return HttpMethod.OPTIONS.matches(request.getMethod().toUpperCase());
    }

    private <T> UsernamePasswordAuthenticationToken getUserAuthenticationToken(T principal) {
        return new UsernamePasswordAuthenticationToken(principal,
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
    }
}
