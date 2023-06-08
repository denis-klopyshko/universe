package com.universe.rest.filter;

import com.universe.entity.CourseEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.universe.repository.CourseRepository.Specs.byProfessorId;
import static com.universe.repository.CourseRepository.Specs.byStudentId;
import static org.springframework.data.jpa.domain.Specification.where;

public class CourseFilter {
    private Long studentId;
    private Long professorId;

    public Specification<CourseEntity> toSpec() {
        Specification<CourseEntity> spec = where((root, query, cb) -> cb.isTrue(cb.literal(true)));
        if (Objects.nonNull(studentId)) {
            spec = spec.and(byStudentId(studentId));
        } else if (Objects.nonNull(professorId)) {
            spec = spec.and(byProfessorId(professorId));
        }

        return spec;
    }
}
