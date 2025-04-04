package com.example.employeemgr.service;

import com.example.employeemgr.model.TemporarySchedule;
import com.example.employeemgr.repository.TemporaryScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TemporaryScheduleService {
    private final TemporaryScheduleRepository temporaryScheduleRepository;

    public TemporaryScheduleService(TemporaryScheduleRepository temporaryScheduleRepository) {
        this.temporaryScheduleRepository = temporaryScheduleRepository;
    }

    public List<TemporarySchedule> getTemporarySchedulesByEmployeeId(Long employeeId) {
        return temporaryScheduleRepository.findByEmployeeId(employeeId);
    }

    public List<TemporarySchedule> getActiveSchedules(Long employeeId) {
        return temporaryScheduleRepository.findByEmployeeIdAndEndDateGreaterThanEqual(employeeId, java.time.LocalDate.now());
    }

    public TemporarySchedule saveSchedule(TemporarySchedule schedule) {
        return temporaryScheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        temporaryScheduleRepository.deleteById(scheduleId);
    }
} 