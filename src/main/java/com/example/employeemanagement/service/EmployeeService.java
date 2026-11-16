package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.CreateEmployeeRequest;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeCodeGenerator codeGenerator;
    private final UtilityService utilityService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeCodeGenerator codeGenerator,
                           UtilityService utilityService) {
        this.employeeRepository = employeeRepository;
        this.codeGenerator = codeGenerator;
        this.utilityService = utilityService;
    }

    public List<Employee> findAll(String department) {
        List<Employee> employees;
        if (department == null || department.isBlank()) {
            employees = employeeRepository.findAll();
        } else {
            employees = employeeRepository.findByDepartmentIgnoreCase(department.trim());
        }
        return employees.stream()
                .sorted(Comparator.comparing(Employee::getId))
                .toList();
    }

    public Employee findById(long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee create(CreateEmployeeRequest request) {
        Employee employee = new Employee(
                codeGenerator.nextCode(),
                utilityService.normalizeName(request.fullName()),
                utilityService.normalizeName(request.department()),
                utilityService.normalizeEmail(request.email())
        );
        return employeeRepository.save(employee);
    }
}
