package com.workshop.platform.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workshop.platform.dto.DashboardStatsResponse;
import com.workshop.platform.entity.Role;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.WorkshopStatus;
import com.workshop.platform.exception.BadRequestException;
import com.workshop.platform.exception.ResourceNotFoundException;
import com.workshop.platform.repository.RegistrationRepository;
import com.workshop.platform.repository.UserRepository;
import com.workshop.platform.repository.WorkshopRepository;
import com.workshop.platform.repository.AttendanceRepository;
import com.workshop.platform.repository.FeedbackRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;
    private final RegistrationRepository registrationRepository;
    private final AttendanceRepository attendanceRepository;
    private final FeedbackRepository feedbackRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository, WorkshopRepository workshopRepository, RegistrationRepository registrationRepository, AttendanceRepository attendanceRepository, FeedbackRepository feedbackRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.workshopRepository = workshopRepository;
        this.registrationRepository = registrationRepository;
        this.attendanceRepository = attendanceRepository;
        this.feedbackRepository = feedbackRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getTeachers() {
        return userRepository.findByRole(Role.TEACHER);
    }

    public List<User> getStudents() {
        return userRepository.findByRole(Role.USER);
    }

    @SuppressWarnings("null")
    public void removeTeacher(Long adminId, Long teacherId) {
        validateAdmin(adminId);
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("Selected user is not a teacher");
        }

        long workshopCount = workshopRepository.countByTeacher(teacher);
        if (workshopCount > 0) {
            throw new BadRequestException("Cannot remove this teacher because they have assigned workshops");
        }

        userRepository.delete(teacher);
    }

    @SuppressWarnings("null")
    public void removeStudent(Long adminId, Long studentId) {
        validateAdmin(adminId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (student.getRole() != Role.USER) {
            throw new BadRequestException("Selected user is not a student");
        }

        if (registrationRepository.countByUser(student) > 0
                || attendanceRepository.countByStudent(student) > 0
                || feedbackRepository.countByStudent(student) > 0) {
            throw new BadRequestException("Cannot remove this student because they have workshop activity");
        }

        userRepository.delete(student);
    }

    public User addTeacher(Long adminId, User teacherRequest) {
        validateAdmin(adminId);

        userRepository.findByEmail(teacherRequest.getEmail()).ifPresent(u -> {
            throw new BadRequestException("Email already exists");
        });

        teacherRequest.setRole(Role.TEACHER);
        teacherRequest.setId(null);
        teacherRequest.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        return userRepository.save(teacherRequest);
    }

    public DashboardStatsResponse getStats(Long adminId) {
        validateAdmin(adminId);

        DashboardStatsResponse response = new DashboardStatsResponse();
        response.setTotalUsers(userRepository.count());
        response.setTotalTeachers(userRepository.findByRole(Role.TEACHER).size());
        response.setTotalStudents(userRepository.findByRole(Role.USER).size());
        response.setTotalWorkshops(workshopRepository.count());
        response.setTotalApproved(workshopRepository.findByStatus(WorkshopStatus.APPROVED).size());
        response.setTotalPending(workshopRepository.findByStatus(WorkshopStatus.PENDING).size());
        response.setTotalRegistrations(registrationRepository.count());
        return response;
    }

    @SuppressWarnings("null")
    private void validateAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new BadRequestException("Only admin can perform this action");
        }
    }
}
