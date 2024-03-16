package com.alien.attendance.service;

import com.alien.attendance.DTO.Auth.LoginDTO;
import com.alien.attendance.DTO.Auth.UserDTO;
import com.alien.attendance.entity.UserEntity;
import com.alien.attendance.repository.UserRepository;
import com.alien.attendance.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class UserService {
    Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createUser(UserDTO userDTO) {
        try {
            UserEntity user = new UserEntity();
            Security security = new Security();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            String hashedPassword = security.hashPassword(userDTO.getPassword());
            user.setPassword(hashedPassword);
            user.setPhone(userDTO.getPhone());
            user.setBorn(userDTO.getBorn());
            user.setEmployee_id(userDTO.getEmployee_id());
            user.setLocation(userDTO.getLocation());
            user.setRole(userDTO.getRole());
            user.setLevel(userDTO.getLevel());
            user.setStatus(userDTO.getStatus());
            user.setJoin_date(userDTO.getJoin_date());
            user.setIs_active("Y");
            UserEntity savedUser = userRepository.save(user);
            logger.info("User Created");
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> login(LoginDTO userDTO) {
        try {
            UserEntity user = userRepository.findOneByEmail(userDTO.getEmail());
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            } else if (user.getIs_active().equals("N")) {
                return ResponseEntity.badRequest().body("User is not active");
            }
            Security security = new Security();
            System.out.println("user : " + user.toString());
            if (security.checkPassword(userDTO.getPassword(), user.getPassword())) {
                Map<String, String> response = new HashMap<>();
                response.put("name", user.getName());
                response.put("email", user.getEmail());
                response.put("role", user.getRole());
                response.put("level", user.getLevel());
                String accessToken = security.createToken(user.getId(), user.getEmail(), user.getName());
                response.put("access_token", accessToken);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body("Invalid Password");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}


