package com.universe.repository;

import com.universe.entity.CourseEntity_;
import com.universe.entity.LessonEntity;
import com.universe.entity.LessonEntity_;
import com.universe.entity.UserEntity_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long>, JpaSpecificationExecutor<LessonEntity> {
    Page<LessonEntity> findAll(Pageable pageable);

    static class Specs {
        public static Specification<LessonEntity> byWeekNumber(Integer weekNumber) {
            return ((root, query, cb) ->
                    cb.equal(root.get(LessonEntity_.WEEK_NUMBER), weekNumber));
        }

        public static Specification<LessonEntity> byDayOfWeek(Integer dayOfWeek) {
            return ((root, query, cb) ->
                    cb.equal(root.get(LessonEntity_.DAY_OF_WEEK), DayOfWeek.of(dayOfWeek)));
        }

        public static Specification<LessonEntity> byCourseName(String courseName) {
            return ((root, query, cb) ->
                    cb.equal(root.join(LessonEntity_.COURSE)
                            .get(CourseEntity_.NAME), courseName));
        }

        public static Specification<LessonEntity> byStudentId(Long studentId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(LessonEntity_.STUDENTS)
                            .get(UserEntity_.ID), studentId));
        }

        public static Specification<LessonEntity> byWeekAndDay(Integer weekNumber, Integer dayOfWeek) {
            return Specification.where(byWeekNumber(weekNumber)).and(byDayOfWeek(dayOfWeek));
        }
    }
}
