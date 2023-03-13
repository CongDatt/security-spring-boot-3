package com.noo.learnjwt.service;

import com.noo.learnjwt.entity.Role;
import com.noo.learnjwt.entity.User;
import com.noo.learnjwt.repository.RoleRepository;
import com.noo.learnjwt.repository.UserRepository;
import com.noo.learnjwt.request.LoginRequest;
import com.noo.learnjwt.request.RegisterRequest;
import com.noo.learnjwt.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(null, "USER"));
        roleRepository.saveAll(roles);

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
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
        log.info("User: {}" , user);
        var jwtToken = jwtService.generate(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
