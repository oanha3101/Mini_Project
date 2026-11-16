package com.example.employeemanagement.service;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UtilityService {

    public String normalizeName(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    public String normalizeEmail(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
