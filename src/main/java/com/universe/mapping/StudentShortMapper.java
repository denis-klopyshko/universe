package com.universe.mapping;

import com.universe.dto.student.StudentShortDto;
import com.universe.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentShortMapper {
    StudentShortMapper INSTANCE = Mappers.getMapper(StudentShortMapper.class);

    StudentShortDto mapToDto(StudentEntity studentEntity);
}
