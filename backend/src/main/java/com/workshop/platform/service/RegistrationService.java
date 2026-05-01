package com.workshop.platform.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.workshop.platform.dto.RegistrationRequest;
import com.workshop.platform.entity.Registration;
import com.workshop.platform.entity.Role;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import com.workshop.platform.entity.WorkshopStatus;
import com.workshop.platform.exception.BadRequestException;
import com.workshop.platform.exception.ResourceNotFoundException;
import com.workshop.platform.repository.RegistrationRepository;
import com.workshop.platform.repository.UserRepository;
import com.workshop.platform.repository.WorkshopRepository;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;

    public RegistrationService(RegistrationRepository registrationRepository, UserRepository userRepository, WorkshopRepository workshopRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.workshopRepository = workshopRepository;
    }

    @SuppressWarnings("null")
    public Registration register(RegistrationRequest request) {
        User student = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (student.getRole() != Role.USER) {
            throw new BadRequestException("Only students can register for workshops");
        }

        Workshop workshop = workshopRepository.findById(request.getWorkshopId())
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));

        if (workshop.getStatus() != WorkshopStatus.APPROVED) {
            throw new BadRequestException("Only approved workshops can be registered");
        }

        registrationRepository.findByUserAndWorkshop(student, workshop).ifPresent(r -> {
            throw new BadRequestException("User already registered for this workshop");
        });

        Registration registration = new Registration();
        registration.setUser(student);
        registration.setWorkshop(workshop);
        registration.setRegisteredAt(LocalDateTime.now());

        return registrationRepository.save(registration);
    }

    @SuppressWarnings("null")
    public List<Registration> getUserRegistrations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return registrationRepository.findByUser(user);
    }

    @SuppressWarnings("null")
    public List<Registration> getWorkshopRegistrations(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        return registrationRepository.findByWorkshop(workshop);
    }

    @SuppressWarnings("null")
    public void cancel(Long registrationId, Long userId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));

        if (!registration.getUser().getId().equals(userId)) {
            throw new BadRequestException("Only registration owner can cancel");
        }

        registrationRepository.delete(registration);
    }
}
