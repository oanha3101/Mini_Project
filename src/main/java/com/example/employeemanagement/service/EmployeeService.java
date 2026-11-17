package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.CreateEmployeeRequest;
import com.example.employeemanagement.dto.UpdateEmployeeRequest;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
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

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
    }

    public Employee create(CreateEmployeeRequest request) {
        String normalizedEmail = utilityService.normalizeEmail(request.email());
        if (employeeRepository.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            throw new DuplicateResourceException("Employee with email '" + request.email() + "' already exists");
        }

        Employee employee = new Employee(
                codeGenerator.nextCode(),
                utilityService.normalizeName(request.fullName()),
                utilityService.normalizeName(request.department()),
                normalizedEmail
        );
        return employeeRepository.save(employee);
    }

    public Employee update(Long id, UpdateEmployeeRequest request) {
        Employee employee = findById(id);

        if (request.fullName() != null && !request.fullName().isBlank()) {
            employee.setFullName(utilityService.normalizeName(request.fullName()));
        }
        if (request.department() != null && !request.department().isBlank()) {
            employee.setDepartment(utilityService.normalizeName(request.department()));
        }
        if (request.email() != null && !request.email().isBlank()) {
            String normalizedEmail = utilityService.normalizeEmail(request.email());
            employeeRepository.findByEmailIgnoreCase(normalizedEmail)
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException("Employee with email '" + request.email() + "' already exists");
                    });
            employee.setEmail(normalizedEmail);
        }

        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
    }

    public List<Employee> search(String keyword) {
        return employeeRepository
                .findByFullNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(keyword, keyword)
                .stream()
                .sorted(Comparator.comparing(Employee::getId))
                .toList();
    }
}
