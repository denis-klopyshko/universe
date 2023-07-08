package com.universe.mapping;

import com.universe.dto.lesson.LessonDto;
import com.universe.entity.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    LessonDto mapToDto(LessonEntity lessonEntity);

    LessonEntity mapToEntity(LessonDto lessonDto);
}
