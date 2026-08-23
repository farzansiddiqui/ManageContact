package com.englishspeaking.app.service;

import com.englishspeaking.app.dto.request.LoginRequest;
import com.englishspeaking.app.dto.request.SignupRequest;
import com.englishspeaking.app.dto.response.UserResponse;
import com.englishspeaking.app.entity.User;
import com.englishspeaking.app.exception.DuplicateEmailException;
import com.englishspeaking.app.exception.InvalidCredentialsException;
import com.englishspeaking.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    private AuthService authService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, passwordEncoder);
    }

    @Test
    void successfulSignup() {
        SignupRequest request = new SignupRequest("Faraz Rahman", "faraz@gmail.com", "Password@123");

        when(userRepository.existsByEmail("faraz@gmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserResponse response = authService.registerUser(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Faraz Rahman", response.name());
        assertEquals("faraz@gmail.com", response.email());
    }

    @Test
    void duplicateEmail() {
        SignupRequest request = new SignupRequest("Faraz Rahman", "faraz@gmail.com", "Password@123");

        when(userRepository.existsByEmail("faraz@gmail.com")).thenReturn(true);

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> authService.registerUser(request));
        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    void successfulLogin() {
        LoginRequest request = new LoginRequest("faraz@gmail.com", "Password@123");

        User user = new User();
        user.setId(1L);
        user.setName("Faraz Rahman");
        user.setEmail("faraz@gmail.com");
        user.setPassword(passwordEncoder.encode("Password@123"));

        when(userRepository.findByEmail("faraz@gmail.com")).thenReturn(Optional.of(user));

        UserResponse response = authService.authenticateUser(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Faraz Rahman", response.name());
        assertEquals("faraz@gmail.com", response.email());
    }

    @Test
    void invalidEmail() {
        LoginRequest request = new LoginRequest("unknown@gmail.com", "Password@123");

        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> authService.authenticateUser(request));
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void invalidPassword() {
        LoginRequest request = new LoginRequest("faraz@gmail.com", "WrongPassword");

        User user = new User();
        user.setId(1L);
        user.setName("Faraz Rahman");
        user.setEmail("faraz@gmail.com");
        user.setPassword(passwordEncoder.encode("Password@123"));

        when(userRepository.findByEmail("faraz@gmail.com")).thenReturn(Optional.of(user));

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> authService.authenticateUser(request));
        assertEquals("Invalid email or password", exception.getMessage());
    }
}
