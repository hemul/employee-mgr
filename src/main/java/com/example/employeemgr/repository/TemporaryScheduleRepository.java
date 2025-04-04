package com.example.employeemgr.repository;

import com.example.employeemgr.model.TemporarySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TemporaryScheduleRepository extends JpaRepository<TemporarySchedule, Long> {
    List<TemporarySchedule> findByEmployeeIdAndEndDateGreaterThanEqual(Long employeeId, LocalDate date);
    List<TemporarySchedule> findByEmployeeId(Long employeeId);
} 