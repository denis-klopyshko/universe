package com.universe.mapping;

import com.universe.dto.course.CourseDto;
import com.universe.entity.CourseEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = StudentShortMapper.class)
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseDto mapToDto(CourseEntity courseEntity);

    @Mapping(target = "students", ignore = true)
    CourseEntity mapToEntity(CourseDto courseDto);

    @IterableMapping(elementTargetType = CourseEntity.class)
    ArrayList<CourseEntity> mapAsList(List<CourseDto> courseDtoList);

    @Mapping(target = "students", ignore = true)
    void updateCourseFromDto(CourseDto courseDto, @MappingTarget CourseEntity courseEntity);
}
