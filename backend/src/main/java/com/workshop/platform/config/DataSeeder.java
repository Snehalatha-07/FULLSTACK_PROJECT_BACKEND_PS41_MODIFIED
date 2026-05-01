package com.workshop.platform.config;

import com.workshop.platform.entity.*;
import com.workshop.platform.repository.UserRepository;
import com.workshop.platform.repository.WorkshopRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;
    private final PasswordEncoder passwordEncoder;

        public DataSeeder(UserRepository userRepository, WorkshopRepository workshopRepository, PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.workshopRepository = workshopRepository;
                this.passwordEncoder = passwordEncoder;
        }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User();
        admin.setName("System Admin");
        admin.setEmail("admin@platform.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        User teacher = new User();
        teacher.setName("Workshop Teacher");
        teacher.setEmail("teacher@platform.com");
        teacher.setPassword(passwordEncoder.encode("teacher123"));
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        User student = new User();
        student.setName("Demo Student");
        student.setEmail("student@platform.com");
        student.setPassword(passwordEncoder.encode("student123"));
        student.setRole(Role.USER);
        userRepository.save(student);

        Workshop approved = new Workshop();
        approved.setTitle("React and Spring Boot Starter Workshop");
        approved.setDescription("Learn the full workflow from frontend to backend integration.");
        approved.setSessionDate(LocalDate.now().plusDays(3));
        approved.setSessionTime(LocalTime.of(10, 0));
        approved.setDurationMinutes(90);
        approved.setMeetingLink("https://meet.google.com/demo-link");
        approved.setStatus(WorkshopStatus.APPROVED);
        approved.setTeacher(teacher);
        workshopRepository.save(approved);

        Workshop pending = new Workshop();
        pending.setTitle("Pending AI Tools Workshop");
        pending.setDescription("This workshop is waiting for admin approval.");
        pending.setSessionDate(LocalDate.now().plusDays(5));
        pending.setSessionTime(LocalTime.of(14, 0));
        pending.setDurationMinutes(60);
        pending.setMeetingLink("https://zoom.us/demo-link");
        pending.setStatus(WorkshopStatus.PENDING);
        pending.setTeacher(teacher);
        workshopRepository.save(pending);
    }
}