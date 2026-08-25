package com.englishspeaking.app.service;

import com.englishspeaking.app.dto.request.LoginRequest;
import com.englishspeaking.app.dto.request.SignupRequest;
import com.englishspeaking.app.dto.response.UserResponse;
import com.englishspeaking.app.entity.User;
import com.englishspeaking.app.exception.DuplicateEmailException;
import com.englishspeaking.app.exception.InvalidCredentialsException;
import com.englishspeaking.app.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUser(SignupRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        User user = new User(request.name().trim(), email, passwordEncoder.encode(request.password()));

        try {
            User savedUser = userRepository.save(user);
            return toUserResponse(savedUser);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateEmailException();
        }
    }

    @Transactional(readOnly = true)
    public UserResponse authenticateUser(LoginRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return toUserResponse(user);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
