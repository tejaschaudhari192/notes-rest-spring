package com.jaybalaji192.notes.services;

import com.jaybalaji192.notes.models.User;
import com.jaybalaji192.notes.repositories.UserRepository;
import com.jaybalaji192.notes.requests.AuthRequest;
import com.jaybalaji192.notes.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
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
        boolean taken = userRepository.findByUsername(username).isPresent();
        logger.debug("Checking if username '{}' is taken: {}", username, taken);
        return taken;
    }

    public void register(AuthRequest request){
        logger.debug("Saving new user '{}' to repository", request.username());
        User user = new User(
                request.username(),
                request.password()
        );
        userRepository.save(user);
    }

    public String login(AuthRequest request){
        logger.debug("Authenticating user '{}'", request.username());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        
        logger.debug("Generating JWT token for user '{}'", request.username());
        return jwtUtil.generateToken(request.username());
    }
}