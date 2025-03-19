package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.LoginRequest;
import com.ecommercesystem.ecombackend.model.RegisterRequest;
import com.ecommercesystem.ecombackend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class) // Enables Mockito in JUnit 5
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegister_ShouldReturnSuccessMessage() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("Prathmesh", "psbiradar948@gmail.com", "Pratham123@", "User");

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn("User Saved Successfully");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Saved Successfully"));
    }

    @Test
    public void testLogin_ShouldReturnJwtToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("Prathmesh", "Pratham123@");

        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn("mockedToken");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("mockedToken"));
    }
}
