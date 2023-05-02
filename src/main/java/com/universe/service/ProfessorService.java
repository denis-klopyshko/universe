package com.universe.service;

import com.universe.dto.professor.ProfessorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public interface ProfessorService {
    Page<ProfessorDto> findAll(Pageable pageable);

    ProfessorDto create(@Valid @NotNull ProfessorDto professorDto);

    ProfessorDto update(@NotNull Long id, @Valid @NotNull ProfessorDto professorDto);

    ProfessorDto findOne(@NotNull Long professorId);

    void delete(@NotNull Long professorId);
}
