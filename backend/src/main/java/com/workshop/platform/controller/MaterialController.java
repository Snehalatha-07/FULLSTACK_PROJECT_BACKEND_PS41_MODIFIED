package com.workshop.platform.controller;

import com.workshop.platform.dto.MaterialRequest;
import com.workshop.platform.dto.MaterialResponse;
import com.workshop.platform.dto.UserSummary;
import com.workshop.platform.entity.Material;
import com.workshop.platform.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<MaterialResponse> addMaterial(@Valid @RequestBody MaterialRequest request) {
        Material m = materialService.addMaterial(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(m));
    }

    @GetMapping("/workshop/{workshopId}")
    public ResponseEntity<List<MaterialResponse>> getWorkshopMaterials(@PathVariable Long workshopId) {
        List<Material> list = materialService.getByWorkshop(workshopId);
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    private MaterialResponse toDto(Material m) {
        if (m == null) return null;
        MaterialResponse r = new MaterialResponse();
        r.setId(m.getId());
        r.setTitle(m.getTitle());
        r.setType(m.getType());
        r.setUrl(m.getUrl());
        r.setPostSession(m.getPostSession());
        try { if (m.getWorkshop() != null) r.setWorkshopId(m.getWorkshop().getId()); } catch (Exception ignore) {}
        try { if (m.getUploadedBy() != null) r.setUploadedBy(new UserSummary(m.getUploadedBy().getId(), m.getUploadedBy().getName(), m.getUploadedBy().getEmail(), m.getUploadedBy().getRole().name())); } catch (Exception ignore) {}
        return r;
    }
}
