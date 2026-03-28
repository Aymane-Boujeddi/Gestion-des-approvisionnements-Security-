package com.gestion.stock.security;

import com.gestion.stock.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        String path = request.getServletPath();
        log.debug("JwtAuthenticationFilter processing: {}", path);
        
        String header = request.getHeader("Authorization");
        log.debug("Authorization header present: {}", header != null);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            log.debug("Token extracted, attempting to identify token type");

            // Try to determine if this is a custom JWT by attempting to parse it
            try {
                String username = jwtUtil.extractUsername(token);
                log.debug("Successfully parsed as custom JWT. Username: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    log.debug("Loading user details for: {}", username);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    log.debug("Authorities: {}", userDetails.getAuthorities());
                    
                    if (jwtUtil.isTokenValid(token, username)) {
                        log.debug("Custom JWT token valid, setting authentication");
                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.debug("Custom JWT authentication set successfully");

                        // Mark request to skip OAuth2 processing
                        request.setAttribute("CUSTOM_JWT_PROCESSED", true);
                    } else {
                        log.warn("Custom JWT token invalid for: {}", username);
                    }
                }
            } catch (Exception e) {
                log.debug("Not a custom JWT token (or invalid), will allow OAuth2 to process: {}", e.getMessage());
                // Not our custom JWT, let OAuth2 resource server handle it
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean skip = request.getServletPath().startsWith("/auth/");
        log.debug("Should skip filter for {}: {}", request.getServletPath(), skip);
        return skip;
    }
}
