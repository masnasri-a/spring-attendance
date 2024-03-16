package com.alien.attendance.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class UserDTO {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Phone is required")
    private String phone;

    @NotNull(message = "Born date is required")
    private String born;

    @NotNull(message = "Employee ID is required")
    private String employee_id;

    @NotNull(message = "Location is required")
    private String location;

    @NotNull(message = "Role is required")
    private String role;

    @NotNull(message = "Level is required")
    private String level;

    @NotNull(message = "Status is required")
    private String status;

    @NotNull(message = "Join date is required")
    private String join_date;

}


