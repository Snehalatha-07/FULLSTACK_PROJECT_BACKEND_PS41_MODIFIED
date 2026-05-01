package com.workshop.platform.repository;

import com.workshop.platform.entity.Registration;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    @Override
    @EntityGraph(attributePaths = {"user", "workshop", "workshop.teacher"})
    Optional<Registration> findById(Long id);

    @EntityGraph(attributePaths = {"user", "workshop", "workshop.teacher"})
    List<Registration> findByUser(User user);

    long countByUser(User user);

    @EntityGraph(attributePaths = {"user", "workshop", "workshop.teacher"})
    List<Registration> findByWorkshop(Workshop workshop);

    @EntityGraph(attributePaths = {"user", "workshop", "workshop.teacher"})
    Optional<Registration> findByUserAndWorkshop(User user, Workshop workshop);
}
