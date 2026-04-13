package com.jaybalaji192.notes.controllers;

import com.jaybalaji192.notes.requests.AuthRequest;
import com.jaybalaji192.notes.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request){
        if (authService.isUsernameTaken(request.username())){
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        
        authService.register(request);
        return ResponseEntity.ok("Registration Successful");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody AuthRequest request){
        Map<String, String> response = new HashMap<>();
        try {
            String token = authService.login(request);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }
}