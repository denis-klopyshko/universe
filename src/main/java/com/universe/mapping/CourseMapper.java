package com.universe.mapping;

import com.universe.dto.course.CourseDto;
import com.universe.entity.CourseEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {StudentShortMapper.class, ProfessorShortMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseDto mapToDto(CourseEntity courseEntity);

    CourseEntity mapToEntity(CourseDto courseDto);

    @IterableMapping(elementTargetType = CourseEntity.class)
    ArrayList<CourseEntity> mapAsList(List<CourseDto> courseDtoList);

    void updateCourseFromDto(CourseDto courseDto, @MappingTarget CourseEntity courseEntity);
}
