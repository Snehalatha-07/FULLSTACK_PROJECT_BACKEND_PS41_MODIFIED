package com.workshop.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import com.workshop.platform.entity.WorkshopStatus;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
    @Override
    @EntityGraph(attributePaths = "teacher")
    Optional<Workshop> findById(Long id);

    @EntityGraph(attributePaths = "teacher")
    List<Workshop> findByTeacher(User teacher);

    long countByTeacher(User teacher);

    @EntityGraph(attributePaths = "teacher")
    List<Workshop> findByStatus(WorkshopStatus status);

        @EntityGraph(attributePaths = "teacher")
        @Query("""
                        select w from Workshop w
                        where (:status is null or w.status = :status)
                            and (:teacherId is null or w.teacher.id = :teacherId)
                            and (
                                        :search is null or :search = ''
                                        or lower(w.title) like lower(concat('%', :search, '%')) escape '\\'
                                        or lower(w.description) like lower(concat('%', :search, '%')) escape '\\'
                                        or lower(w.teacher.name) like lower(concat('%', :search, '%')) escape '\\'
                            )
                        order by w.sessionDate desc, w.sessionTime desc
                        """)
        List<Workshop> searchWorkshops(@Param("search") String search, @Param("teacherId") Long teacherId, @Param("status") WorkshopStatus status);
}
