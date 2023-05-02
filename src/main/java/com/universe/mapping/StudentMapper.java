package com.universe.mapping;

import com.universe.dto.student.StudentDto;
import com.universe.entity.StudentEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.DayOfWeek;

@Mapper(uses = {LessonShortMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDto mapToDto(StudentEntity studentEntity);

    StudentEntity mapToEntity(StudentDto studentDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "courses", ignore = true)
    StudentEntity mapBaseAttributes(StudentDto studentDto);

    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "group", ignore = true)
    void updateStudentFromDto(StudentDto studentDto, @MappingTarget StudentEntity studentEntity);

    default DayOfWeek mapDayOfWeek(Integer dayOfWeek) {
        return DayOfWeek.of(dayOfWeek);
    }
}