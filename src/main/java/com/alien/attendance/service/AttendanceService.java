package com.alien.attendance.service;

import com.alien.attendance.DTO.Attendance.ClockDTO;
import com.alien.attendance.entity.AttendanceEntity;
import com.alien.attendance.repository.AttendanceRepository;
import com.alien.attendance.util.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public ResponseEntity<?> clock(Map<String, Object> body, ClockDTO clockDTO) {
        Location location = new Location();
        String loc = location.getLocation(clockDTO.getLatitude(), clockDTO.getLongitude());
        Long id = Long.parseLong(body.get("user_id").toString());
        AttendanceEntity entity = new AttendanceEntity();
        entity.setClock_type(clockDTO.getType());
        entity.setLatlong(clockDTO.getLatitude() + "," + clockDTO.getLongitude());
        entity.setUser_id(id);
        entity.setCreated_at(new Date());
        entity.setLocation(loc);
        attendanceRepository.save(entity);
        return ResponseEntity.ok().body(entity);
    }

    public ResponseEntity<?> getClock(Map<String, Object> body) {
        Long userId = Long.parseLong(body.get("user_id").toString());
        List<AttendanceEntity> listData = attendanceRepository.findAttendanceByClockTypeAndUserIdAndCreatedAt("ClockIn", userId, "2024-03-01");
        System.out.println(listData);
        return ResponseEntity.ok().body(body);
    }
}
