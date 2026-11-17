package com.example.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateEmployeeRequest(
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        String fullName,

        @Size(max = 50, message = "Department must not exceed 50 characters")
        String department,

        @Email(message = "Email must be a valid email address")
        String email
) {
}
