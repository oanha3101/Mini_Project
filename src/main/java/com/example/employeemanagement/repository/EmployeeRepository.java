package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.DepartmentStatistics;
import com.example.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartmentIgnoreCase(String department);

    Optional<Employee> findByEmailIgnoreCase(String email);

    List<Employee> findByFullNameContainingIgnoreCase(String fullName);

    List<Employee> findByFullNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(String name, String department);

    @Query("SELECT new com.example.employeemanagement.dto.DepartmentStatistics(e.department, COUNT(e)) FROM Employee e GROUP BY e.department")
    List<DepartmentStatistics> countByDepartment();

    @Query("SELECT COUNT(e) FROM Employee e")
    long countTotal();
}
