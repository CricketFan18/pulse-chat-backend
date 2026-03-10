package com.app.ChatApplication.service;


import com.app.ChatApplication.dto.AuthResponse;
import com.app.ChatApplication.dto.LoginRequest;
import com.app.ChatApplication.dto.RegisterRequest;
import com.app.ChatApplication.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.ChatApplication.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void registerUser(RegisterRequest request)
    {
        if(userRepository.findByEmail(request.email()).isPresent() || userRepository.findByUsername(request.username()).isPresent())
            throw new RuntimeException();

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.username(),request.email(),hashedPassword);
        userRepository.save(user);
    }

    public AuthResponse loginUser(LoginRequest request) throws Exception {

        Optional<User> user = userRepository.findByEmail(request.email());
        if(user.isEmpty())
            throw new Exception();

        if(!passwordEncoder.matches(request.password(),user.get().getPassword()))
            throw  new Exception();

        return  new AuthResponse(jwtService.generateToken(request.email()));
    }
}
