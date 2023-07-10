package com.universe.mapping;

import com.universe.dto.professor.ProfessorDto;
import com.universe.entity.ProfessorEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessorDtoMapperTest {
    private static final ProfessorMapper MAPPER = ProfessorMapper.INSTANCE;

    @Test
    void shouldMapEntityToDto() {
        ProfessorEntity professorEntity = ProfessorEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Snow")
                .build();

        ProfessorDto professorDto = MAPPER.mapToDto(professorEntity);

        assertThat(professorDto)
                .usingRecursiveComparison()
                .isEqualTo(professorEntity);
    }

    @Test
    void shouldMapBaseAttributesFromDtoToEntity() {
        ProfessorDto professorDto = ProfessorDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Snow")
                .build();

        ProfessorEntity professorEntity = MAPPER.mapBaseAttributes(professorDto);

        assertThat(professorEntity.getId()).isNull();
        assertThat(professorEntity.getFirstName()).isEqualTo(professorDto.getFirstName());
        assertThat(professorEntity.getLastName()).isEqualTo(professorDto.getLastName());
    }

    @Test
    void shouldUpdateEntityWithDto() {
        ProfessorDto professorDto = ProfessorDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Snow")
                .build();

        ProfessorEntity professorEntity = ProfessorEntity.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();


        MAPPER.updateProfessorFromDto(professorDto, professorEntity);

        assertThat(professorEntity.getId()).isEqualTo(1L);
        assertThat(professorEntity.getFirstName()).isEqualTo(professorDto.getFirstName());
        assertThat(professorEntity.getLastName()).isEqualTo(professorDto.getLastName());
    }
}
