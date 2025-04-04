package com.example.employeemgr.service;

import com.example.employeemgr.model.Employee;
import com.example.employeemgr.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        logger.debug("Fetching all employees from database");
        List<Employee> employees = employeeRepository.findAll();
        logger.debug("Found {} employees", employees.size());
        return employees;
    }

    public Optional<Employee> getEmployeeById(Long id) {
        logger.debug("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id);
    }

    public Employee saveEmployee(Employee employee) {
        logger.debug("Saving employee: {}", employee);
        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Successfully saved employee with ID: {}", savedEmployee.getId());
        return savedEmployee;
    }

    public void deleteEmployee(Long id) {
        logger.debug("Deleting employee with ID: {}", id);
        employeeRepository.deleteById(id);
        logger.info("Successfully deleted employee with ID: {}", id);
    }
} 