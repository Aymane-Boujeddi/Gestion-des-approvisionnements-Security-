package com.gestion.stock.service.impl;

import com.gestion.stock.dto.request.AuthRequestDTO;
import com.gestion.stock.dto.request.RegisterRequestDTO;
import com.gestion.stock.dto.response.AuthResponseDTO;
import com.gestion.stock.dto.response.RegisterResponseDTO;
import com.gestion.stock.entity.User;
import com.gestion.stock.exception.DuplicateResourceException;
import com.gestion.stock.repository.UserRepository;
import com.gestion.stock.service.AuthService;
import com.gestion.stock.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponseDTO handleLogin(AuthRequestDTO authRequestDTO) throws AuthenticationException {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(),
                        authRequestDTO.getPassword()
                )
        );

        String username = authentication.getName();
        String token = jwtUtil.generateToken(username);

        return AuthResponseDTO.builder()
                .message("Login successful")
                .username(username)
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public RegisterResponseDTO handleRegister(RegisterRequestDTO registerRequestDTO) {
        
        if (userRepository.findUserByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists: " + registerRequestDTO.getUsername());
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        User newUser = User.builder()
                .username(registerRequestDTO.getUsername())
                .password(encodedPassword)
                .role(null)
                .build();

        userRepository.save(newUser);

        return RegisterResponseDTO.builder()
                .message("User registered successfully. Please wait for admin to assign your role.")
                .username(newUser.getUsername())
                .build();
    }
}