package com.englishspeaking.app.controller;

import com.englishspeaking.app.dto.request.LoginRequest;
import com.englishspeaking.app.dto.request.SignupRequest;
import com.englishspeaking.app.entity.User;
import com.englishspeaking.app.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void signupShouldCreateUser() throws Exception {
        SignupRequest request = new SignupRequest("Faraz Rahman", "faraz@gmail.com", "Password@123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.user.email").value("faraz@gmail.com"));
    }

    @Test
    void duplicateEmailShouldBeRejected() throws Exception {
        userRepository.save(new User("Faraz Rahman", "faraz@gmail.com", passwordEncoder.encode("Password@123")));

        SignupRequest request = new SignupRequest("Faraz Rahman", "faraz@gmail.com", "Password@123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Email already registered"));
    }

    @Test
    void loginShouldSucceedWithCorrectPassword() throws Exception {
        userRepository.save(new User("Faraz Rahman", "faraz@gmail.com", passwordEncoder.encode("Password@123")));

        LoginRequest request = new LoginRequest("faraz@gmail.com", "Password@123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.user.email").value("faraz@gmail.com"));
    }

    @Test
    void loginShouldFailWithWrongPassword() throws Exception {
        userRepository.save(new User("Faraz Rahman", "faraz@gmail.com", passwordEncoder.encode("Password@123")));

        LoginRequest request = new LoginRequest("faraz@gmail.com", "WrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

        @Test
        void signupShouldRejectInvalidInput() throws Exception {
                String invalidRequest = """
                                {
                                    "name": "",
                                    "email": "invalid-email",
                                    "password": "short"
                                }
                                """;

                mockMvc.perform(post("/api/auth/signup")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(invalidRequest))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Validation failed"))
                                .andExpect(jsonPath("$.errors.name").exists())
                                .andExpect(jsonPath("$.errors.email").exists())
                                .andExpect(jsonPath("$.errors.password").exists())
                                .andExpect(jsonPath("$.timestamp").exists());
        }
}
