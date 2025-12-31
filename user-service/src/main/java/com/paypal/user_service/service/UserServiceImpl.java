package com.paypal.user_service.service;

import com.paypal.user_service.client.WalletClient;
import com.paypal.user_service.dto.*;
import com.paypal.user_service.entity.User;
import com.paypal.user_service.exception.BadRequestException;
import com.paypal.user_service.exception.ResourceNotFoundException;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.util.JWTUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    WalletClient walletClient;
    PasswordEncoder passwordEncoder;
    JWTUtil jwtUtil;
    @Override
    public SignUpResponse createUser(SignUpRequest request) {

        log.info("User signup initiated for email: {}", request.email());

        if (userRepository.findByEmail(request.email()).isPresent()) {
            log.warn("Signup failed - user already exists with email: {}", request.email());
            throw new BadRequestException("User already exists with email : " + request.email());
        }

        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .role("ROLE_USER")
                .build();

        log.debug("User entity created for email: {}", user.getEmail());

        User savedUser = userRepository.save(user);
        log.info("User saved successfully with ID: {}", savedUser.getId());

        try {
            CreateWalletRequest createWalletRequest = CreateWalletRequest.builder()
                    .userId(savedUser.getId())
                    .currency("INR")
                    .build();

            log.info("Wallet creation started for userId: {}", savedUser.getId());
            walletClient.createWallet(createWalletRequest);
            log.info("Wallet created successfully for userId: {}", savedUser.getId());

        } catch (Exception ex) {
            log.error("Wallet creation failed for userId: {}. Rolling back user creation.",
                    savedUser.getId(), ex);

            userRepository.deleteById(savedUser.getId());

            throw new RuntimeException("Wallet creation failed, user rolled back", ex);
        }

        log.info("Signup completed successfully for userId: {}", savedUser.getId());

        return new SignUpResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {

        log.info("Login attempt for email: {}", loginRequest.getEmail());

        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            log.warn("Login failed - user not found for email: {}", loginRequest.getEmail());
            throw new ResourceNotFoundException("User", loginRequest.getEmail());
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Login failed - invalid password for email: {}", loginRequest.getEmail());
            throw new BadRequestException("❌ Invalid credentials");
        }

        log.info("Login successful for userId: {}", user.getId());

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        log.debug("JWT generated successfully for userId: {}", user.getId());

        return LoginResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }



    @Override
    public Optional<User> getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);
        return userRepository.findById(id);
    }



    @Override
    public List<User> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll();
    }
}
