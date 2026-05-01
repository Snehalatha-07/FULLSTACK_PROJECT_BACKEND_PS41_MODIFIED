package com.workshop.platform.repository;

import com.workshop.platform.entity.Material;
import com.workshop.platform.entity.Workshop;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    @EntityGraph(attributePaths = {"workshop", "workshop.teacher", "uploadedBy"})
    List<Material> findByWorkshop(Workshop workshop);
}
