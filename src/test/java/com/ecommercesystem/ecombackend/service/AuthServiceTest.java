package com.ecommercesystem.ecombackend.service;

import com.ecommercesystem.ecombackend.model.LoginRequest;
import com.ecommercesystem.ecombackend.model.RegisterRequest;
import com.ecommercesystem.ecombackend.model.Role;
import com.ecommercesystem.ecombackend.model.User;
import com.ecommercesystem.ecombackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp()
    {
        registerRequest = new RegisterRequest("Prathmesh","psbiradar948@gmail.com","Pratham123@","USER");
        loginRequest= new LoginRequest("Prathmesh","Pratham123@");
        user= new User();
        user.setUsername("Prathmesh");
        user.setEmail("psbiradar948@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
    }

    @Test
    public void testRegisterUser_shouldReturnSuccessMessage_WhenUserDoesNotExist(){
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String response = authService.registerUser(registerRequest);
        assertEquals("User Saved Successfully",response);
    }
    @Test
    public void testRegisterUser_ShouldReturnErrorMessage_WhenUserAlreadyExists()
    {
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(user));

        String response = authService.registerUser(registerRequest);
        assertEquals("User already exist",response);
    }
    @Test
    public void authenticateUser_ShouldReturnToken_WhenCredentialsAreValid()
    {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getUsername())).thenReturn("mockedToken");

        String token = authService.authenticateUser(loginRequest);
        assertEquals("mockedToken",token);
    }
    @Test
    public void authenticateUser_ShouldThrowException_WhenUserNotFound()
    {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,()->authService.authenticateUser(loginRequest));
        assertEquals("User Not Found",exception.getMessage());
    }
    @Test
    public void authenticateUser_ShouldThrowException_WhenPasswordIsIncorrect(){
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class,()->authService.authenticateUser(loginRequest));
        assertEquals("Invalid Credentials",exception.getMessage());
    }

}
