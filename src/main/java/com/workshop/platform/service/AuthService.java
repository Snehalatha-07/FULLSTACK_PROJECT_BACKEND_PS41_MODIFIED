package com.workshop.platform.service;

import com.workshop.platform.dto.AuthRequest;
import com.workshop.platform.dto.AuthResponse;
import com.workshop.platform.dto.RegisterRequest;
import com.workshop.platform.entity.User;
import com.workshop.platform.exception.BadRequestException;
import com.workshop.platform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());
        String name = normalizeText(request.getName(), 80);

        userRepository.findByEmail(email).ifPresent(u -> {
            throw new BadRequestException("Email already exists");
        });

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public AuthResponse login(AuthRequest request) {
        String email = normalizeEmail(request.getEmail());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(jwtService.generateToken(user));
        return response;
    }

    private String normalizeEmail(String email) {
        return normalizeText(email, 255).toLowerCase();
    }

    private String normalizeText(String input, int maxLen) {
        if (input == null) {
            return null;
        }

        String normalized = input
                .replaceAll("[\\p{Cntrl}]", " ")
                .trim()
                .replaceAll("\\s+", " ");

        if (normalized.length() > maxLen) {
            throw new BadRequestException("Input is too long");
        }

        return normalized;
    }
}
