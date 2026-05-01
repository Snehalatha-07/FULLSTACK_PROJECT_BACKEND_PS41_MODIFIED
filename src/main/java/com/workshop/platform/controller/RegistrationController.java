package com.workshop.platform.controller;

import com.workshop.platform.dto.RegistrationRequest;
import com.workshop.platform.dto.RegistrationResponse;
import com.workshop.platform.dto.UserSummary;
import com.workshop.platform.dto.WorkshopResponse;
import com.workshop.platform.entity.Registration;
import com.workshop.platform.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        Registration reg = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(reg));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RegistrationResponse>> getUserRegistrations(@PathVariable Long userId) {
        List<Registration> list = registrationService.getUserRegistrations(userId);
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    @GetMapping("/workshop/{workshopId}")
    public ResponseEntity<List<RegistrationResponse>> getWorkshopRegistrations(@PathVariable Long workshopId) {
        List<Registration> list = registrationService.getWorkshopRegistrations(workshopId);
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    private RegistrationResponse toDto(Registration reg) {
        if (reg == null) return null;
        RegistrationResponse r = new RegistrationResponse();
        r.setId(reg.getId());
        r.setRegisteredAt(reg.getRegisteredAt());
        try {
            if (reg.getUser() != null) {
                r.setUser(new UserSummary(reg.getUser().getId(), reg.getUser().getName(), reg.getUser().getEmail(), reg.getUser().getRole().name()));
            }
        } catch (Exception ignore) {}
        try {
            if (reg.getWorkshop() != null) {
                WorkshopResponse wr = new WorkshopResponse();
                wr.setId(reg.getWorkshop().getId());
                wr.setTitle(reg.getWorkshop().getTitle());
                r.setWorkshop(wr);
            }
        } catch (Exception ignore) {}
        return r;
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long registrationId, @RequestParam Long userId) {
        registrationService.cancel(registrationId, userId);
        return ResponseEntity.noContent().build();
    }
}
