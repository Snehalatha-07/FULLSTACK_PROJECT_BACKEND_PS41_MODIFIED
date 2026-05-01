package com.workshop.platform.repository;

import com.workshop.platform.entity.Feedback;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @EntityGraph(attributePaths = {"workshop", "workshop.teacher", "student"})
    List<Feedback> findByWorkshop(Workshop workshop);

    @EntityGraph(attributePaths = {"workshop", "workshop.teacher", "student"})
    Optional<Feedback> findByWorkshopAndStudent(Workshop workshop, User student);

    @EntityGraph(attributePaths = {"workshop", "workshop.teacher", "student"})
    List<Feedback> findByStudent(User student);

    long countByStudent(User student);
}
