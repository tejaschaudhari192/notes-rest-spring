package com.jaybalaji192.notes.services;

import com.jaybalaji192.notes.models.User;
import com.jaybalaji192.notes.repositories.UserRepository;
import com.jaybalaji192.notes.requests.AuthRequest;
import com.jaybalaji192.notes.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void register(AuthRequest request){
        User user = new User(
                request.username(),
                request.password()
        );
        userRepository.save(user);
    }

    public String login(AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        return jwtUtil.generateToken(request.username());
    }
}
