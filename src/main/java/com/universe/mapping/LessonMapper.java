package com.universe.mapping;

import com.universe.dto.lesson.LessonDto;
import com.universe.entity.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.DayOfWeek;

@Mapper
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    LessonDto mapToDto(LessonEntity lessonEntity);

    default Integer map(DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue();
    }
}
