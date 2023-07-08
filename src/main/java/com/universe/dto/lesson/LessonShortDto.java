package com.universe.dto.lesson;

import com.universe.dto.RoomDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonShortDto {
    private Long id;

    private String courseName;

    private String professorFullName;

    private GroupShortDto group;

    private RoomDto room;

    private Integer order;

    private LessonType type;

    private Integer weekNumber;

    private String dayOfWeek;
}
