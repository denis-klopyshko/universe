package com.universe.rest.filter;

import com.universe.entity.LessonEntity;
import com.universe.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.Specification;

import java.time.DayOfWeek;
import java.util.Objects;

import static com.universe.repository.LessonRepository.Specs.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonFilter {
    private String courseName;

    private Long professorId;

    private String roomCode;
    @Range(min = 1, max = 45, message = "Week Number should be between 1 and 45.")
    private Integer weekNumber;
    private DayOfWeek dayOfWeek;

    private LessonType lessonType;

    @Range(min = 1, max = 7, message = "Lesson order should be from 1 to 7.")
    private Integer order;

    public Specification<LessonEntity> toSpec() {
        Specification<LessonEntity> spec = where((root, query, cb) -> cb.isTrue(cb.literal(true)));
        if (Objects.nonNull(weekNumber)) {
            spec = spec.and(byWeekNumber(weekNumber));
        } else if (Objects.nonNull(dayOfWeek)) {
            spec = spec.and(byDayOfWeek(dayOfWeek));
        } else if (Objects.nonNull(courseName)) {
            spec = spec.and(byCourseName(courseName));
        } else if (Objects.nonNull(roomCode)) {
            spec = spec.and(byRoomCode(roomCode));
        }

        return spec;
    }
}
