package com.universe.service.impl;

import com.universe.dto.professor.ProfessorDto;
import com.universe.entity.ProfessorEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.ProfessorMapper;
import com.universe.repository.LessonRepository;
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

import static com.universe.repository.LessonRepository.Specs.byProfessorId;
import static java.lang.String.format;


@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {
    private static final ProfessorMapper MAPPER = ProfessorMapper.INSTANCE;
    private final ProfessorRepository professorRepo;

    private final UserRepository userRepository;

    private final LessonRepository lessonRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessorDto> findAll(Pageable pageable) {
        return professorRepo.findAll(pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    public ProfessorDto create(ProfessorDto professorDto) {
        validateUserExistsByEmail(professorDto.getEmail());
        log.info("Creating professor: {}", professorDto);
        var professorEntity = MAPPER.mapBaseAttributes(professorDto);
        var createdProfessor = professorRepo.save(professorEntity);
        return MAPPER.mapToDto(createdProfessor);
    }

    @Override
    public ProfessorDto update(Long id, ProfessorDto professorDto) {
        log.info("Updating professor with ID [{}]. Payload: {}", id, professorDto);
        var professorEntity = findProfessorEntity(id);
        if (!professorEntity.getEmail().equals(professorDto.getEmail())) {
            validateUserExistsByEmail(professorDto.getEmail());
        }

        MAPPER.updateProfessorFromDto(professorDto, professorEntity);
        var updatedProfessor = professorRepo.save(professorEntity);

        return MAPPER.mapToDto(updatedProfessor);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessorDto findOne(Long professorId) {
        var professorEntity = findProfessorEntity(professorId);
        var professorLessons = lessonRepo.findAll(byProfessorId(professorId));
        if (!professorLessons.isEmpty()) {
            throw new ConflictException("Can't delete professor. Professor has assigned lessons!");
        }
        return MAPPER.mapToDto(professorEntity);
    }

    @Override
    public void delete(Long professorId) {
        var professorEntity = findProfessorEntity(professorId);
        var lessons = lessonRepo.findAll(byProfessorId(professorId));
        if (!lessons.isEmpty()) {
            throw new ConflictException("Can't delete professor. Professor has assigned lessons!");
        }

        professorRepo.delete(professorEntity);
    }

    private ProfessorEntity findProfessorEntity(Long professorId) {
        return professorRepo.findById(professorId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Professor with id: %s not found!", professorId))
                );
    }

    private void validateUserExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(format("User with email: %s already exists!", email));
        }
    }
}
