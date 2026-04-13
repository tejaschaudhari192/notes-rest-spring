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
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User addUser(AuthRequest request){
        User user = new User(
                request.username(),
                request.password()
        );
        return userRepository.save(user);
    }

    public String login(AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(),request.password())
        );
        String token = jwtUtil.generateToken(request.username());
        return token;
    }
}
