package example.recipeapi.controller;

import example.recipeapi.model.User;
import example.recipeapi.repository.UserRepository;
import example.recipeapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/authenticate")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Username already exists");
        }
        
        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        
        // Encode password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("User registered successfully!");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User userRequest) {
        try {
            // Attempt authentication
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userRequest.getUsername(), 
                    userRequest.getPassword()
                )
            );
            
            // Generate token
            String token = jwtUtil.generateToken(authentication);
            
            // Return token in response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userRequest.getUsername());
            
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred during login");
        }
    }
}