package com.universe.repository;

import com.universe.entity.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    Page<CourseEntity> findAll(Pageable pageable);

    Optional<CourseEntity> findByName(String name);

    @Query("select distinct c from CourseEntity c join fetch c.students s where s.id = ?1")
    List<CourseEntity> findAllByStudentId(Long studentId);
}
