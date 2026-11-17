package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.CreateEmployeeRequest;
import com.example.employeemanagement.dto.UpdateEmployeeRequest;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

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
        log.info("Fetching employees list, department filter: {}", department);
        List<Employee> employees;
        if (department == null || department.isBlank()) {
            employees = employeeRepository.findAll();
        } else {
            employees = employeeRepository.findByDepartmentIgnoreCase(department.trim());
        }
        log.debug("Found {} employees", employees.size());
        return employees.stream()
                .sorted(Comparator.comparing(Employee::getId))
                .toList();
    }

    public Employee findById(Long id) {
        log.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee", id);
                });
    }

    public Employee create(CreateEmployeeRequest request) {
        log.info("Creating new employee: name={}, department={}, email={}",
                request.fullName(), request.department(), request.email());

        String normalizedEmail = utilityService.normalizeEmail(request.email());
        if (employeeRepository.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            log.warn("Duplicate email detected: {}", request.email());
            throw new DuplicateResourceException("Employee with email '" + request.email() + "' already exists");
        }

        Employee employee = new Employee(
                codeGenerator.nextCode(),
                utilityService.normalizeName(request.fullName()),
                utilityService.normalizeName(request.department()),
                normalizedEmail
        );
        Employee saved = employeeRepository.save(employee);
        log.info("Employee created successfully: id={}, code={}", saved.getId(), saved.getEmployeeCode());
        return saved;
    }

    public Employee update(Long id, UpdateEmployeeRequest request) {
        log.info("Updating employee with id: {}", id);
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
                        log.warn("Duplicate email detected during update: {}", request.email());
                        throw new DuplicateResourceException("Employee with email '" + request.email() + "' already exists");
                    });
            employee.setEmail(normalizedEmail);
        }

        Employee updated = employeeRepository.save(employee);
        log.info("Employee updated successfully: id={}, code={}", updated.getId(), updated.getEmployeeCode());
        return updated;
    }

    public void delete(Long id) {
        log.info("Deleting employee with id: {}", id);
        Employee employee = findById(id);
        employeeRepository.delete(employee);
        log.info("Employee deleted successfully: id={}, code={}", employee.getId(), employee.getEmployeeCode());
    }

    public List<Employee> search(String keyword) {
        log.info("Searching employees with keyword: {}", keyword);
        List<Employee> results = employeeRepository
                .findByFullNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(keyword, keyword)
                .stream()
                .sorted(Comparator.comparing(Employee::getId))
                .toList();
        log.debug("Search returned {} results for keyword: {}", results.size(), keyword);
        return results;
    }
}
