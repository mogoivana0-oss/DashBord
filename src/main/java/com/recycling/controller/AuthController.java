package com.recycling.controller;

import com.recycling.dto.JwtAuthenticationResponse;
import com.recycling.dto.LoginRequest;
import com.recycling.model.User;
import com.recycling.security.JwtTokenProvider;
import com.recycling.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur pour l'authentification
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    /**
     * Connexion utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String jwt = tokenProvider.generateToken(authentication);
        User user = userService.getUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return ResponseEntity.ok(
                JwtAuthenticationResponse.builder()
                        .accessToken(jwt)
                        .userId(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build()
        );
    }

    /**
     * Vérifier si le token est valide
     */
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (tokenProvider.validateToken(token)) {
                return ResponseEntity.ok(new java.util.HashMap<String, Object>()
                    .put("valid", true)
                    .put("username", tokenProvider.getUsernameFromToken(token))
                );
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new java.util.HashMap<String, String>()
                    .put("error", "Token invalide ou expiré")
                );
    }
}
