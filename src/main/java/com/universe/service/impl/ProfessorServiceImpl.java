package com.universe.service.impl;

import com.universe.dto.professor.ProfessorDto;
import com.universe.entity.ProfessorEntity;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.ProfessorMapper;
import com.universe.repository.ProfessorRepository;
import com.universe.repository.UserRepository;
import com.universe.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {
    private static final ProfessorMapper MAPPER = ProfessorMapper.INSTANCE;
    private final ProfessorRepository professorRepo;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessorDto> findAll(Pageable pageable) {
        return professorRepo.findAll(pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    public ProfessorDto create(ProfessorDto professorDto) {
        log.info("Creating professor: {}", professorDto);
        var professorEntity = MAPPER.mapBaseAttributes(professorDto);
        var createdProfessor = professorRepo.save(professorEntity);

//        if (!professorDto.getCourses().isEmpty()) {
//            professorDto.getCourses()
//                    .forEach(courseDto -> assignProfessorOnCourse(createdProfessor, courseDto.getId()));
//        }

        return MAPPER.mapToDto(createdProfessor);
    }

    @Override
    public ProfessorDto update(Long id, ProfessorDto professorDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessorDto findOne(Long professorId) {
        var professorEntity = findProfessorEntity(professorId);
        return MAPPER.mapToDto(professorEntity);
    }

    @Override
    public void delete(Long professorId) {
        var professorEntity = findProfessorEntity(professorId);
        professorRepo.delete(professorEntity);
    }

    private ProfessorEntity findProfessorEntity(Long professorId) {
        return professorRepo.findById(professorId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Professor with id: %s not found!", professorId))
                );
    }
}
