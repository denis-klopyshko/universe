package com.universe.repository;

import com.universe.entity.GroupEntity_;
import com.universe.entity.StudentEntity;
import com.universe.entity.StudentEntity_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long>, JpaSpecificationExecutor<StudentEntity> {
    @Query("select distinct s from StudentEntity s join fetch s.courses c where c.name = ?1")
    List<StudentEntity> findAllByCourseName(String courseName);

    // fix N+1 problem
    @Query("select distinct s from StudentEntity s join fetch s.courses c where s.id = ?1")
    Optional<StudentEntity> findByIdFetchCourses(Long id);

    List<StudentEntity> findAllByEmailIn(Collection<String> emails);

    @UtilityClass
    static class Specs {
        public static Specification<StudentEntity> withGroupId(Long groupId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(StudentEntity_.GROUP).get(GroupEntity_.ID), groupId));
        }

        public static Specification<StudentEntity> withGroupName(String groupName) {
            return ((root, query, cb) ->
                    cb.equal(root.join(StudentEntity_.GROUP).get(GroupEntity_.NAME), groupName));
        }
    }
}
