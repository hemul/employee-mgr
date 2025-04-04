package com.example.employeemgr.controller;

import com.example.employeemgr.model.Employee;
import com.example.employeemgr.model.TemporarySchedule;
import com.example.employeemgr.service.EmployeeService;
import com.example.employeemgr.service.TemporaryScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@Controller
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    private final EmployeeService employeeService;
    private final TemporaryScheduleService temporaryScheduleService;

    public WebController(EmployeeService employeeService, TemporaryScheduleService temporaryScheduleService) {
        this.employeeService = employeeService;
        this.temporaryScheduleService = temporaryScheduleService;
    }

    @GetMapping("/")
    public String listEmployees(Model model) {
        logger.info("Fetching all employees");
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.debug("Showing add employee form");
        model.addAttribute("employee", new Employee());
        return "add-employee";
    }

    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee, 
                            BindingResult result) {
        logger.debug("Processing add employee request for employee: {}", employee.getEmail());
        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while adding employee: {}", result.getAllErrors());
            return "add-employee";
        }
        employeeService.saveEmployee(employee);
        logger.info("Successfully added new employee with email: {}", employee.getEmail());
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.debug("Showing edit form for employee ID: {}", id);
        try {
            Employee employee = employeeService.getEmployeeById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
            model.addAttribute("employee", employee);
            return "edit-employee";
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching employee with ID: {}", id, e);
            throw e;
        }
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id, 
                               @Valid @ModelAttribute("employee") Employee employee,
                               BindingResult result) {
        logger.debug("Processing update request for employee ID: {}", id);
        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while updating employee: {}", result.getAllErrors());
            employee.setId(id);
            return "edit-employee";
        }
        employee.setId(id);
        employeeService.saveEmployee(employee);
        logger.info("Successfully updated employee with ID: {}", id);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        logger.debug("Processing delete request for employee ID: {}", id);
        try {
            employeeService.getEmployeeById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
            employeeService.deleteEmployee(id);
            logger.info("Successfully deleted employee with ID: {}", id);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting employee with ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/employee/{id}/schedule")
    public String showScheduleForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        
        model.addAttribute("employee", employee);
        model.addAttribute("temporarySchedule", new TemporarySchedule());
        model.addAttribute("activeSchedules", temporaryScheduleService.getActiveSchedules(id));
        return "schedule-form";
    }

    @PostMapping("/employee/{id}/schedule")
    public String addTemporarySchedule(@PathVariable Long id, 
                                     @Valid @ModelAttribute TemporarySchedule temporarySchedule,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return "schedule-form";
        }

        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        
        temporarySchedule.setEmployee(employee);
        temporaryScheduleService.saveSchedule(temporarySchedule);
        return "redirect:/employee/" + id + "/schedule";
    }

    @GetMapping("/employee/{employeeId}/schedule/{scheduleId}/delete")
    public String deleteTemporarySchedule(@PathVariable Long employeeId, 
                                        @PathVariable Long scheduleId) {
        temporaryScheduleService.deleteSchedule(scheduleId);
        return "redirect:/employee/" + employeeId + "/schedule";
    }

    @GetMapping("/employee/{id}/info")
    public String showEmployeeInfo(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
        
        List<TemporarySchedule> temporarySchedules = temporaryScheduleService.getTemporarySchedulesByEmployeeId(id);
        
        model.addAttribute("employee", employee);
        model.addAttribute("temporarySchedules", temporarySchedules);
        
        return "employee-info";
    }

    @GetMapping("/administration")
    public String showAdministrationPage(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "administration";
    }
} 