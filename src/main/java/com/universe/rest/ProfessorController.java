package com.universe.rest;

import com.universe.dto.professor.ProfessorDto;
import com.universe.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/professors")
public class ProfessorController {
    private final ProfessorService professorService;

    @GetMapping
    public Page<ProfessorDto> findAll(Pageable pageable) {
        return professorService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ProfessorDto getOneById(@PathVariable Long id) {
        return professorService.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessorDto create(@RequestBody ProfessorDto professorDto) {
        return professorService.create(professorDto);
    }

    @PutMapping("/{id}")
    public ProfessorDto update(@PathVariable Long id, @RequestBody ProfessorDto professorDto) {
        return professorService.update(id, professorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        professorService.delete(id);
    }
}
