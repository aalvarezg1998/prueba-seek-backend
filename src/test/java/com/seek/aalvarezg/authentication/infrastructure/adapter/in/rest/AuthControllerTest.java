package com.seek.aalvarezg.authentication.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seek.aalvarezg.authentication.application.dto.AuthResponse;
import com.seek.aalvarezg.authentication.application.port.in.LoginInputPort;
import com.seek.aalvarezg.authentication.application.port.in.RegisterUserInputPort;
import com.seek.aalvarezg.authentication.domain.exception.InvalidCredentialsException;
import com.seek.aalvarezg.authentication.domain.exception.UserAlreadyExistsException;
import com.seek.aalvarezg.authentication.infrastructure.adapter.in.rest.dto.LoginRequestDto;
import com.seek.aalvarezg.authentication.infrastructure.adapter.in.rest.dto.RegisterRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AuthController Integration Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterUserInputPort registerUserInputPort;

    @MockBean
    private LoginInputPort loginInputPort;

    @Test
    @DisplayName("POST /api/v1/auth/register should return 201")
    void register_withValidData_shouldReturn201() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto("test@example.com", "password123");
        AuthResponse response = new AuthResponse("jwt.token", "test@example.com");
        when(registerUserInputPort.execute(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt.token"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register with invalid email should return 422")
    void register_withInvalidEmail_shouldReturn422() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto("invalid-email", "password123");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("POST /api/v1/auth/register when user exists should return 409")
    void register_whenUserExists_shouldReturn409() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto("test@example.com", "password123");
        when(registerUserInputPort.execute(any()))
                .thenThrow(new UserAlreadyExistsException("test@example.com"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should return 200")
    void login_withValidCredentials_shouldReturn200() throws Exception {
        LoginRequestDto request = new LoginRequestDto("test@example.com", "password123");
        AuthResponse response = new AuthResponse("jwt.token", "test@example.com");
        when(loginInputPort.execute(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login with invalid credentials should return 401")
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        LoginRequestDto request = new LoginRequestDto("test@example.com", "wrongpassword");
        when(loginInputPort.execute(any()))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login with blank fields should return 422")
    void login_withBlankFields_shouldReturn422() throws Exception {
        LoginRequestDto request = new LoginRequestDto("", "");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}
