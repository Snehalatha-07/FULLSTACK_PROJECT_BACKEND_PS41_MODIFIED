package com.workshop.platform.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.workshop.platform.dto.AttendanceRequest;
import com.workshop.platform.entity.Attendance;
import com.workshop.platform.entity.Registration;
import com.workshop.platform.entity.Role;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import com.workshop.platform.exception.BadRequestException;
import com.workshop.platform.exception.ResourceNotFoundException;
import com.workshop.platform.repository.AttendanceRepository;
import com.workshop.platform.repository.RegistrationRepository;
import com.workshop.platform.repository.UserRepository;
import com.workshop.platform.repository.WorkshopRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final WorkshopRepository workshopRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, WorkshopRepository workshopRepository, UserRepository userRepository, RegistrationRepository registrationRepository) {
        this.attendanceRepository = attendanceRepository;
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
    }

    @SuppressWarnings("null")
    public Attendance markAttendance(AttendanceRequest request) {
        Workshop workshop = workshopRepository.findById(request.getWorkshopId())
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("Only teachers can mark attendance");
        }

        if (student.getRole() != Role.USER) {
            throw new BadRequestException("Attendance can be marked only for students");
        }

        if (!workshop.getTeacher().getId().equals(teacher.getId())) {
            throw new BadRequestException("Only workshop owner can mark attendance");
        }

        Registration registration = registrationRepository.findByUserAndWorkshop(student, workshop)
                .orElseThrow(() -> new BadRequestException("Student is not registered for this workshop"));

        if (registration == null) {
            throw new BadRequestException("Student is not registered for this workshop");
        }

        Attendance attendance = attendanceRepository.findByWorkshopAndStudent(workshop, student)
            .orElseGet(() -> {
                Attendance item = new Attendance();
                item.setWorkshop(workshop);
                item.setStudent(student);
                item.setMarkedBy(teacher);
                return item;
            });

        attendance.setPresent(request.getPresent());
        attendance.setMarkedBy(teacher);
        attendance.setMarkedAt(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }

    @SuppressWarnings("null")
    public List<Attendance> getWorkshopAttendance(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        return attendanceRepository.findByWorkshop(workshop);
    }
}
