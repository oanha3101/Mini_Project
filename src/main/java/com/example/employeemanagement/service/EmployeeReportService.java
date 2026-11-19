package com.example.employeemanagement.service;

import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeReportService {

    private final EmployeeRepository employeeRepository;

    public EmployeeReportService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(value = "employeeCount", key = "#root.methodName")
    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }
}
