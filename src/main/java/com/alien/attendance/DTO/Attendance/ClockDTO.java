package com.alien.attendance.DTO.Attendance;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClockDTO {
    @NotNull(message = "Type is required")
    String type;
    @NotNull(message = "Longitude is required")
    String longitude;
    @NotNull(message = "Latitude is required")
    String latitude;
}
