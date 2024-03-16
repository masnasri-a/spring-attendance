package com.alien.attendance.controller;

import com.alien.attendance.DTO.Auth.LoginDTO;
import com.alien.attendance.DTO.Auth.UserDTO;
import com.alien.attendance.repository.UserRepository;
import com.alien.attendance.service.UserService;
import com.alien.attendance.util.BodyValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/auth")
@Validated
public class AuthController {

    private final BodyValidation bodyValidation;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public AuthController(BodyValidation bodyValidation, UserRepository userRepository) {
        this.bodyValidation = bodyValidation;
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @PostMapping("/create_user")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO user) {
        try {
            ResponseEntity<?> validationResponse = bodyValidation.validateBody(user);
            if (!validationResponse.getStatusCode().is2xxSuccessful()) {
                return validationResponse;
            }
            UserService userService = new UserService(this.userRepository);
            return userService.createUser(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            ResponseEntity<?> validationResponse = bodyValidation.validateBody(loginDTO);
            if (!validationResponse.getStatusCode().is2xxSuccessful()) {
                return validationResponse;
            }
            UserService userService = new UserService(this.userRepository);
            return userService.login(loginDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

}