package com.universe.dto.lesson;

import com.universe.dto.RoomDto;
import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.dto.professor.ProfessorShortDto;
import com.universe.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private Long id;

    @NotNull
    @Valid
    private CourseShortDto course;

    @NotNull
    @Valid
    private ProfessorShortDto professor;

    @NotNull
    @Valid
    private GroupShortDto group;

    @NotNull
    @Valid
    private RoomDto room;

    @NotNull
    @Range(min = 1, max = 7, message = "Lesson order should be from 1 to 7.")
    private Integer order;

    @NotNull
    private LessonType type;

    @NotNull
    @Range(min = 1, max = 45, message = "Week Number should be between 1 and 45.")
    private Integer weekNumber;

    @NotNull
    private DayOfWeek dayOfWeek;
}
