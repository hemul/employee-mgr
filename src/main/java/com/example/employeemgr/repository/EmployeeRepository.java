package com.example.employeemgr.repository;

import com.example.employeemgr.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
} 