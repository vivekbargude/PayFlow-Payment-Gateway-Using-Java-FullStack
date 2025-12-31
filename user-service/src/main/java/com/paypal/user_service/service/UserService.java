package com.paypal.user_service.service;

import com.paypal.user_service.dto.LoginRequest;
import com.paypal.user_service.dto.LoginResponse;
import com.paypal.user_service.dto.SignUpRequest;
import com.paypal.user_service.dto.SignUpResponse;
import com.paypal.user_service.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    SignUpResponse createUser(SignUpRequest signUpRequest);

    Optional<User> getUserById(Long id);

    LoginResponse loginUser(LoginRequest loginRequest);

    List<User> getAllUsers();
}
