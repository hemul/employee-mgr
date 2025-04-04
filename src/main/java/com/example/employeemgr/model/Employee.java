package com.example.employeemgr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    private String department;
    private String position;

    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @ElementCollection
    @CollectionTable(
        name = "employee_office_days",
        joinColumns = @JoinColumn(name = "employee_id")
    )
    @Column(name = "office_day")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> officeDays = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemporarySchedule> temporarySchedules = new ArrayList<>();

    public Set<DayOfWeek> getEffectiveOfficeDays(LocalDate date) {
        return temporarySchedules.stream()
                .filter(schedule -> !date.isBefore(schedule.getStartDate()) && !date.isAfter(schedule.getEndDate()))
                .findFirst()
                .map(TemporarySchedule::getOfficeDays)
                .orElse(officeDays);
    }
} 