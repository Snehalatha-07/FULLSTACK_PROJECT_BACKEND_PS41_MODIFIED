package com.workshop.platform.repository;

import com.workshop.platform.entity.Attendance;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @EntityGraph(attributePaths = {"workshop", "workshop.teacher", "student", "markedBy"})
    List<Attendance> findByWorkshop(Workshop workshop);

    @EntityGraph(attributePaths = {"workshop", "workshop.teacher", "student", "markedBy"})
    Optional<Attendance> findByWorkshopAndStudent(Workshop workshop, User student);

    long countByStudent(User student);
}
