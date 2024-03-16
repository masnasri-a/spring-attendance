package com.alien.attendance.controller;

import com.alien.attendance.DTO.Attendance.ClockDTO;
import com.alien.attendance.repository.AttendanceRepository;
import com.alien.attendance.service.AttendanceService;
import com.alien.attendance.util.BodyValidation;
import com.alien.attendance.util.Security;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/api/attendance")
public class AttendanceController {

    private final BodyValidation bodyValidation;

    @Autowired
    private final AttendanceRepository attendanceRepository;

    public AttendanceController(BodyValidation bodyValidation, AttendanceRepository AttendanceRepository) {
        this.bodyValidation = bodyValidation;
        this.attendanceRepository = AttendanceRepository;
    }


    @PostMapping("/clock")
    public ResponseEntity<?> clock(@Valid @RequestBody ClockDTO body, @RequestHeader("Authorization") String token) {
        try {
            Security security = new Security();
            ResponseEntity<?> validationResponse = security.validateToken(token);
            if (!validationResponse.getStatusCode().is2xxSuccessful()) {
                return validationResponse;
            }
            ResponseEntity<?> validationBodyResponse = bodyValidation.validateBody(body);
            if (!validationBodyResponse.getStatusCode().is2xxSuccessful()) {
                return validationBodyResponse;
            }
            Map<String, Object> claim = security.payloadToken(token);
            AttendanceService service = new AttendanceService(this.attendanceRepository);
            return service.clock(claim, body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/getClock")
    public ResponseEntity<?> getClock(@RequestHeader("Authorization") String token) {
        try {
            Security security = new Security();
            ResponseEntity<?> validationResponse = security.validateToken(token);
            if (!validationResponse.getStatusCode().is2xxSuccessful()) {
                return validationResponse;
            }

            Map<String, Object> claim = security.payloadToken(token);

            AttendanceService service = new AttendanceService(this.attendanceRepository);
            return service.getClock(claim);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

}
