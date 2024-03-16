package com.alien.attendance.repository;

import com.alien.attendance.entity.AttendanceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    String queryFindAttendanceByClockTypeAndUserIdAndCreatedAt = "SELECT * FROM `attendance` WHERE CAST(created_at AS DATE) = '2024-03-17' AND user_id = 1 ORDER BY created_at DESC;";

    @Query(value = queryFindAttendanceByClockTypeAndUserIdAndCreatedAt, nativeQuery = true)
    List<AttendanceEntity> findAttendanceByClockTypeAndUserIdAndCreatedAt(String clockType, Long userId, String dateNow);
}
