package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.DepartmentStatistics;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final EmployeeRepository employeeRepository;

    public StatisticsController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalEmployees() {
        return ResponseEntity.ok(employeeRepository.countTotal());
    }

    @GetMapping("/by-department")
    public ResponseEntity<List<DepartmentStatistics>> getEmployeesByDepartment() {
        return ResponseEntity.ok(employeeRepository.countByDepartment());
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalEmployees", employeeRepository.countTotal());
        summary.put("byDepartment", employeeRepository.countByDepartment());
        return ResponseEntity.ok(summary);
    }
}
