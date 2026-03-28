package com.gestion.stock.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomBearerTokenResolver implements BearerTokenResolver {

    private final DefaultBearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest request) {
        // Check if custom JWT was already processed
        Boolean customJwtProcessed = (Boolean) request.getAttribute("CUSTOM_JWT_PROCESSED");

        if (Boolean.TRUE.equals(customJwtProcessed)) {
            log.debug("Custom JWT already processed, skipping OAuth2 token resolution");
            return null; // Skip OAuth2 processing
        }

        String token = defaultResolver.resolve(request);
        log.debug("OAuth2 token resolution: {}", token != null ? "Token found" : "No token");
        return token;
    }
}
