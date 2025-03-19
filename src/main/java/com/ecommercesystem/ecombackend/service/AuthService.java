package com.ecommercesystem.ecombackend.service;

import com.ecommercesystem.ecombackend.model.LoginRequest;
import com.ecommercesystem.ecombackend.model.RegisterRequest;
import com.ecommercesystem.ecombackend.model.Role;
import com.ecommercesystem.ecombackend.model.User;
import com.ecommercesystem.ecombackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public String registerUser(RegisterRequest request) {

            Optional<User> existingUser = userRepository.findByUsername(request.getUsername());

            if(existingUser.isPresent())
            {
                return "User already exist";
            }
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        try {
            Role userRole = Role.valueOf(request.getRole().toUpperCase());
            // Optional: You could add a check here to restrict certain roles.
            user.setRole(userRole);
        } catch (Exception e) {
            // If the role is missing or invalid, default to USER.
            user.setRole(Role.USER);
        }

             userRepository.save(user);
             return "User Saved Successfully";


    }

    public String authenticateUser(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->
                new RuntimeException("User Not Found"));


        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Invalid Credentials");
        }

        return jwtService.generateToken(user.getUsername());
    }
}
