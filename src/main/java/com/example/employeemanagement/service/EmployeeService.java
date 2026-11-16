package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.CreateEmployeeRequest;
import com.example.employeemanagement.model.Employee;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {
    private final ConcurrentHashMap<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();
    private final EmployeeCodeGenerator codeGenerator;
    private final UtilityService utilityService;

    public EmployeeService(EmployeeCodeGenerator codeGenerator, UtilityService utilityService) {
        this.codeGenerator = codeGenerator;
        this.utilityService = utilityService;
    }

    public List<Employee> findAll(String department) {
        return employees.values().stream()
                .filter(employee -> department == null || department.isBlank()
                        || employee.department().equalsIgnoreCase(utilityService.normalizeName(department)))
                .sorted(Comparator.comparing(Employee::id))
                .toList();
    }

    public Employee findById(long id) {
        return employees.get(id);
    }

    public Employee create(CreateEmployeeRequest request) {
        long id = idSequence.incrementAndGet();
        Employee employee = new Employee(
                id,
                codeGenerator.nextCode(),
                utilityService.normalizeName(request.fullName()),
                utilityService.normalizeName(request.department()),
                utilityService.normalizeEmail(request.email())
        );
        employees.put(id, employee);
        return employee;
    }
}
