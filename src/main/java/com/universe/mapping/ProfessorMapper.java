package com.universe.mapping;

import com.universe.dto.professor.ProfessorDto;
import com.universe.entity.ProfessorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfessorMapper {
    ProfessorMapper INSTANCE = Mappers.getMapper(ProfessorMapper.class);

    ProfessorDto mapToDto(ProfessorEntity professor);

    ProfessorEntity mapToEntity(ProfessorDto professorDto);

    void updateProfessorFromDto(ProfessorDto professorDto, @MappingTarget ProfessorEntity professor);
}
