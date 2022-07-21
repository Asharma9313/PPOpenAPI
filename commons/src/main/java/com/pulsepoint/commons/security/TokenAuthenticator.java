package com.pulsepoint.commons.security;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Objects;

@Component
@RefreshScope
@Log4j2
public class TokenAuthenticator {

    private static final String SECURITY_TOKEN_NAME = "token";

    @Value("${security.token.validateurl}")
    private String tokenValidatorUrl;

    @Value("${appAuthenticationToken}")
    private String specialAuthToken;

    @Autowired
    private RestTemplate restTemplate;

    public UserContextDTO authenticate(HttpServletRequest request) {
        final String token = getValidatedAuthenticationToken(request);
        UserContextDTO userContextDTO;

        if (isSpecialToken(token)) {
            log.debug("Special authentication token found!");
            userContextDTO = new UserContextDTO();
            userContextDTO.setSpecialUser(true);
        } else {
            userContextDTO = getUserContextFromAuthenticationService(request);
            log.debug("Security API response: {}", userContextDTO);
        }
        validate(userContextDTO);
        log.info("Authentication successful!");
        return userContextDTO;
    }

    private UserContextDTO getUserContextFromAuthenticationService(HttpServletRequest request) {
        RequestEntity<Void> requestEntity;
        String token = getTokenFrom(request);

        requestEntity = RequestEntity.get(getAuthenticationUrl())
                .header(SECURITY_TOKEN_NAME, token)
                .header("accountId", getAccountIdFrom(request))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        log.debug("Calling Security API for user authentication...");
        log.debug("Request:\n{}", requestEntity.toString());
        return restTemplate.exchange(requestEntity, UserContextDTO.class).getBody();
    }

    private boolean isSpecialToken(String token) {
        return token.equals(specialAuthToken);
    }

    private String getAccountIdFrom(HttpServletRequest request) {
        return request.getHeader("AccountId");
    }

    private void validate(UserContextDTO userContextDTO) {
        if (Objects.isNull(userContextDTO)) {
            throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "Bad Token"));
        }
    }

    private URI getAuthenticationUrl() {
        URI uri = null;
        try {
            uri = new URIBuilder(tokenValidatorUrl).build();
        } catch (URISyntaxException e) {
            log.info("Invalid Authentication URL {}", tokenValidatorUrl);
            throw new RuntimeException("Invalid Authentication url", e);
        }
        return uri;
    }

    private String getValidatedAuthenticationToken(HttpServletRequest request) {
        String token = getTokenFrom(request);
        validate(token);
        return token;
    }

    private String getTokenFrom(HttpServletRequest request) {
        String headerToken = request.getHeader(SECURITY_TOKEN_NAME);
        String paramToken = request.getParameter(SECURITY_TOKEN_NAME);
        return StringUtils.firstNonBlank(paramToken, headerToken);
    }

    private void validate(String token) {
        if (Objects.isNull(token)) {
            throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "No token found"));
        }
    }
}
