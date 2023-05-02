package com.universe.mapping;

import com.universe.dto.student.StudentShortDto;
import com.universe.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentShortMapper {
    StudentShortMapper INSTANCE = Mappers.getMapper(StudentShortMapper.class);

    @Mapping(target = "groupName", source = "group.name")
    StudentShortDto mapToDto(StudentEntity studentEntity);
}
