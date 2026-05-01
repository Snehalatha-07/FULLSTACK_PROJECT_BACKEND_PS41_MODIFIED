package com.workshop.platform.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.workshop.platform.dto.FeedbackRequest;
import com.workshop.platform.entity.Feedback;
import com.workshop.platform.entity.Registration;
import com.workshop.platform.entity.Role;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import com.workshop.platform.exception.BadRequestException;
import com.workshop.platform.exception.ResourceNotFoundException;
import com.workshop.platform.repository.FeedbackRepository;
import com.workshop.platform.repository.RegistrationRepository;
import com.workshop.platform.repository.UserRepository;
import com.workshop.platform.repository.WorkshopRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;
    private final RegistrationRepository registrationRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository, WorkshopRepository workshopRepository, RegistrationRepository registrationRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.workshopRepository = workshopRepository;
        this.registrationRepository = registrationRepository;
    }

    @SuppressWarnings("null")
    public Feedback submitFeedback(FeedbackRequest request) {
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (student.getRole() != Role.USER) {
            throw new BadRequestException("Only students can submit feedback");
        }

        Workshop workshop = workshopRepository.findById(request.getWorkshopId())
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));

        Registration registration = registrationRepository.findByUserAndWorkshop(student, workshop)
                .orElseThrow(() -> new BadRequestException("Student is not registered for this workshop"));

        if (registration == null) {
            throw new BadRequestException("Student is not registered for this workshop");
        }

        feedbackRepository.findByWorkshopAndStudent(workshop, student).ifPresent(f -> {
            throw new BadRequestException("Feedback already submitted for this workshop");
        });

        Feedback feedback = new Feedback();
        feedback.setWorkshop(workshop);
        feedback.setStudent(student);
        feedback.setRating(request.getRating());
        feedback.setComments(request.getComments());
        feedback.setSubmittedAt(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    @SuppressWarnings("null")
    public List<Feedback> getWorkshopFeedback(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        return feedbackRepository.findByWorkshop(workshop);
    }

    @SuppressWarnings("null")
    public List<Feedback> getStudentFeedback(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return feedbackRepository.findByStudent(student);
    }
}
