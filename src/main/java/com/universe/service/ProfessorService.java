package com.universe.service;

import com.universe.dto.professor.ProfessorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public interface ProfessorService {
    Page<ProfessorDto> findAll(Pageable pageable);

    ProfessorDto findOne(@NotNull Long professorId);
}
