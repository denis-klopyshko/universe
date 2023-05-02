package com.universe.mapping;

import com.universe.dto.lesson.LessonShortDto;
import com.universe.entity.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.DayOfWeek;

@Mapper
public interface LessonShortMapper {
    LessonShortMapper INSTANCE = Mappers.getMapper(LessonShortMapper.class);

    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "professorFullName", expression = "java(lessonEntity.getProfessor().getFullName())")
    LessonShortDto mapToDto(LessonEntity lessonEntity);

    default Integer mapDayOfWeek(DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue();
    }
}
