package com.universe.mapping;

import com.universe.dto.course.CourseShortDto;
import com.universe.entity.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseShortMapper {
    CourseShortMapper INSTANCE = Mappers.getMapper(CourseShortMapper.class);

    CourseShortDto mapToDto(CourseEntity courseEntity);
}
