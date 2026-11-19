package com.example.employeemanagement.dto;

public class DepartmentStatistics {

    private String department;
    private long count;

    public DepartmentStatistics(String department, long count) {
        this.department = department;
        this.count = count;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
