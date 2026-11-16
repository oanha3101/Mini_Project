package com.example.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateEmployeeRequest(
        @NotBlank String fullName,
        @NotBlank String department,
        @NotBlank @Email String email
) {
}
