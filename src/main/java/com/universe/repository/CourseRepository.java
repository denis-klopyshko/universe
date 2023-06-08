package com.universe.repository;

import com.universe.entity.CourseEntity;
import com.universe.entity.CourseEntity_;
import com.universe.entity.ProfessorEntity_;
import com.universe.entity.StudentEntity_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {
    Optional<CourseEntity> findByName(String name);

    @UtilityClass
    class Specs {
        public static Specification<CourseEntity> byStudentId(Long studentId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(CourseEntity_.STUDENTS).get(StudentEntity_.ID), studentId));
        }

        public static Specification<CourseEntity> byProfessorId(Long professorId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(CourseEntity_.PROFESSORS).get(ProfessorEntity_.ID), professorId));
        }
    }
}
