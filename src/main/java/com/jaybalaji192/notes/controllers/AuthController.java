package com.jaybalaji192.notes.controllers;

import com.jaybalaji192.notes.models.User;
import com.jaybalaji192.notes.repositories.UserRepository;
import com.jaybalaji192.notes.requests.AuthRequest;
import com.jaybalaji192.notes.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request){
        if (userRepository.findByUsername(request.username()).isPresent()){
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.username(),hashedPassword);
        userRepository.save(user);
        return ResponseEntity.ok("Registration Successful");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody AuthRequest request){
            Map<String, String> response = new HashMap<>();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            String token = jwtUtil.generateToken(request.username());

            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e);
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }
}
