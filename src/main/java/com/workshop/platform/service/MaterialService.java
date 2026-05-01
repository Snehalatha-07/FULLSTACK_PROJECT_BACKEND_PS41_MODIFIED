package com.workshop.platform.service;

import com.workshop.platform.dto.MaterialRequest;
import com.workshop.platform.entity.Material;
import com.workshop.platform.entity.Role;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import com.workshop.platform.exception.BadRequestException;
import com.workshop.platform.exception.ResourceNotFoundException;
import com.workshop.platform.repository.MaterialRepository;
import com.workshop.platform.repository.UserRepository;
import com.workshop.platform.repository.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final WorkshopRepository workshopRepository;
    private final UserRepository userRepository;

    public MaterialService(MaterialRepository materialRepository, WorkshopRepository workshopRepository, UserRepository userRepository) {
        this.materialRepository = materialRepository;
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    public Material addMaterial(MaterialRequest request) {
        Workshop workshop = workshopRepository.findById(request.getWorkshopId())
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("Only teachers can upload materials");
        }

        if (!workshop.getTeacher().getId().equals(teacher.getId())) {
            throw new BadRequestException("Only workshop owner can upload materials");
        }

        Material material = new Material();
        material.setWorkshop(workshop);
        material.setUploadedBy(teacher);
        material.setTitle(request.getTitle());
        material.setType(request.getType());
        material.setUrl(request.getUrl());
        material.setPostSession(request.getPostSession());

        return materialRepository.save(material);
    }

    @SuppressWarnings("null")
    public List<Material> getByWorkshop(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        return materialRepository.findByWorkshop(workshop);
    }
}
