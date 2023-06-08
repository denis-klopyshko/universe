package com.universe.mapping;

import com.universe.dto.professor.ProfessorShortDto;
import com.universe.entity.ProfessorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfessorShortMapper {
    ProfessorShortMapper INSTANCE = Mappers.getMapper(ProfessorShortMapper.class);

    ProfessorShortDto mapToDto(ProfessorEntity professorEntity);
}
