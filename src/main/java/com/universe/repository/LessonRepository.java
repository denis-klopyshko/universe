package com.universe.repository;

import com.universe.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long>, JpaSpecificationExecutor<LessonEntity> {
    static class Specs {
        public static Specification<LessonEntity> byWeekNumber(Integer weekNumber) {
            return ((root, query, cb) ->
                    cb.equal(root.get(LessonEntity_.WEEK_NUMBER), weekNumber));
        }

        public static Specification<LessonEntity> byDayOfWeek(DayOfWeek dayOfWeek) {
            return ((root, query, cb) ->
                    cb.equal(root.get(LessonEntity_.DAY_OF_WEEK), dayOfWeek));
        }

        public static Specification<LessonEntity> byOrder(int order) {
            return ((root, query, cb) ->
                    cb.equal(root.get(LessonEntity_.ORDER), order));
        }

        public static Specification<LessonEntity> byCourseName(String courseName) {
            return ((root, query, cb) ->
                    cb.equal(root.join(LessonEntity_.COURSE)
                            .get(CourseEntity_.NAME), courseName));
        }

        public static Specification<LessonEntity> byGroupId(Long groupId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(LessonEntity_.GROUP)
                            .get(GroupEntity_.ID), groupId));
        }

        public static Specification<LessonEntity> byProfessorId(Long professorId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(LessonEntity_.PROFESSOR)
                            .get(ProfessorEntity_.ID), professorId));
        }

        public static Specification<LessonEntity> byCourseId(Long courseId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(LessonEntity_.COURSE)
                            .get(CourseEntity_.ID), courseId));
        }

        public static Specification<LessonEntity> byRoomId(Long roomId) {
            return ((root, query, cb) ->
                    cb.equal(root.join(LessonEntity_.ROOM)
                            .get(RoomEntity_.ID), roomId));
        }

        public static Specification<LessonEntity> byRoomCode(String roomCode) {
            return ((root, query, cb) ->
                    cb.equal(root.join(LessonEntity_.ROOM)
                            .get(RoomEntity_.CODE), roomCode));
        }
    }
}
