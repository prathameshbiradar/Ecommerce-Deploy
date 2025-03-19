package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.LoginRequest;
import com.ecommercesystem.ecombackend.model.RegisterRequest;
import com.ecommercesystem.ecombackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<String> test()
    {
        return ResponseEntity.ok( "Test");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest)
    {
        return ResponseEntity.ok(authService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.authenticateUser(request));
    }

}
