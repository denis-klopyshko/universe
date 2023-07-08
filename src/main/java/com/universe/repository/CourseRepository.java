package com.universe.repository;

import com.universe.entity.CourseEntity;
import com.universe.entity.CourseEntity_;
import com.universe.entity.StudentEntity_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {
    List<CourseEntity> findAllByNameIn(Collection<String> names);

    boolean existsByName(String name);

    @UtilityClass
    class Specs {
        public static Specification<CourseEntity> byStudentId(Long studentId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(CourseEntity_.STUDENTS).get(StudentEntity_.ID), studentId));
        }
    }
}
