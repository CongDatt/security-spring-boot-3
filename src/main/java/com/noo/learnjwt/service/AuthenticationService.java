package com.noo.learnjwt.service;

import com.noo.learnjwt.entity.Role;
import com.noo.learnjwt.entity.User;
import com.noo.learnjwt.repository.UserRepository;
import com.noo.learnjwt.request.LoginRequest;
import com.noo.learnjwt.request.RegisterRequest;
import com.noo.learnjwt.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(null, "USER"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(roles)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generate(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generate(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
