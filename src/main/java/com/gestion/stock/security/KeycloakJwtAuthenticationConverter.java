package com.gestion.stock.security;

import com.gestion.stock.entity.User;
import com.gestion.stock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        log.debug("Processing Keycloak JWT token");

        String sub = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        
        if (username == null || username.isBlank()) {
            username = "kc_" + sub.substring(0, Math.min(8, sub.length()));
        }

        final String finalUsername = username;
        User user = userRepository.findByAuthProviderAndClientIdSubWithPermissions("KEYCLOAK", sub)
                .orElseGet(() -> createKeycloakUser(sub, finalUsername));

        log.debug("Keycloak user loaded: {}, has role: {}", user.getUsername(), user.getRole() != null);

        List<GrantedAuthority> authorities = buildAuthorities(user);
        log.debug("Keycloak user authorities: {}", authorities);

        CustomUserDetails principal = new CustomUserDetails(user, authorities);
        
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    private User createKeycloakUser(String sub, String preferredUsername) {
        String username = makeUsernameUnique(preferredUsername);

        User user = User.builder()
                .username(username)
                .password(null)
                .authProvider("KEYCLOAK")
                .clientIdSub(sub)
                .role(null)
                .build();

        return userRepository.save(user);
    }

    private String makeUsernameUnique(String base) {
        if (!userRepository.existsByUsername(base)) {
            return base;
        }
        return base + "_" + System.currentTimeMillis();
    }

    private List<GrantedAuthority> buildAuthorities(User user) {
        if (user.getRole() == null) {
            return List.of();
        }

        Set<String> finalPermissions = new HashSet<>();
        
        if (user.getRole().getDefaultPermissions() != null) {
            user.getRole().getDefaultPermissions().forEach(permission ->
                    finalPermissions.add(permission.getName())
            );
        }

        if (user.getUserPermissions() != null) {
            user.getUserPermissions().forEach(userPerm -> {
                if (userPerm.getPermission() != null) {
                    String permName = userPerm.getPermission().getName();
                    if (userPerm.isGranted()) {
                        finalPermissions.add(permName);
                    } else {
                        finalPermissions.remove(permName);
                    }
                }
            });
        }

        return new ArrayList<>(finalPermissions.stream()
                .map(SimpleGrantedAuthority::new)
                .toList());
    }
}
