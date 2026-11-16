package com.example.employeemanagement.service;

import java.util.concurrent.atomic.AtomicLong;

public class EmployeeCodeGenerator {
    private final String prefix;
    private final AtomicLong sequence = new AtomicLong(1000);

    public EmployeeCodeGenerator(String prefix) {
        this.prefix = prefix;
    }

    public String nextCode() {
        return prefix + sequence.incrementAndGet();
    }
}
