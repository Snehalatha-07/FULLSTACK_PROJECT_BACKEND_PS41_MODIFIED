package com.workshop.platform.controller;

import com.workshop.platform.dto.FeedbackRequest;
import com.workshop.platform.dto.FeedbackResponse;
import com.workshop.platform.dto.UserSummary;
import com.workshop.platform.dto.WorkshopResponse;
import com.workshop.platform.entity.Feedback;
import com.workshop.platform.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        Feedback f = feedbackService.submitFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(f));
    }

    @GetMapping("/workshop/{workshopId}")
    public ResponseEntity<List<FeedbackResponse>> getWorkshopFeedback(@PathVariable Long workshopId) {
        List<Feedback> list = feedbackService.getWorkshopFeedback(workshopId);
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<FeedbackResponse>> getStudentFeedback(@PathVariable Long studentId) {
        List<Feedback> list = feedbackService.getStudentFeedback(studentId);
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    private FeedbackResponse toDto(Feedback f) {
        if (f == null) return null;
        FeedbackResponse r = new FeedbackResponse();
        r.setId(f.getId());
        r.setRating(f.getRating());
        r.setComments(f.getComments());
        r.setSubmittedAt(f.getSubmittedAt());
        try { if (f.getStudent() != null) r.setStudent(new UserSummary(f.getStudent().getId(), f.getStudent().getName(), f.getStudent().getEmail(), f.getStudent().getRole().name())); } catch (Exception ignore) {}
        try { if (f.getWorkshop() != null) { WorkshopResponse w = new WorkshopResponse(); w.setId(f.getWorkshop().getId()); w.setTitle(f.getWorkshop().getTitle()); r.setWorkshop(w); } } catch (Exception ignore) {}
        return r;
    }
}
