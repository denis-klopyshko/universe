package com.universe.service;

import com.universe.dto.professor.ProfessorDto;
import com.universe.entity.LessonEntity;
import com.universe.entity.ProfessorEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.repository.LessonRepository;
import com.universe.repository.ProfessorRepository;
import com.universe.repository.UserRepository;
import com.universe.service.impl.ProfessorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ProfessorServiceImpl.class})
public class ProfessorServiceImplTest {
    @MockBean
    private ProfessorRepository professorRepo;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private LessonRepository lessonRepo;

    @Autowired
    private ProfessorServiceImpl professorService;

    @Test
    void shouldCreateNewProfessor() {
        var professorEntity = getProfessorEntity();
        professorEntity.setId(1L);

        when(professorRepo.save(any(ProfessorEntity.class))).thenReturn(professorEntity);
        when(userRepo.existsByEmail(anyString())).thenReturn(false);

        var professorDto = getProfessorDto();
        var createdProfessor = professorService.create(professorDto);

        assertThat(createdProfessor)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(professorDto);

        assertThat(createdProfessor.getId()).isNotNull();
    }

    @Test
    void shouldNotCreate_UserAlreadyExists() {
        var professorDto = getProfessorDto();
        when(userRepo.existsByEmail(professorDto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> professorService.create(professorDto))
                .isInstanceOf(ConflictException.class)
                .hasMessage("User with email: %s already exists!", professorDto.getEmail());
    }

    @Test
    void shouldNotDelete_ProfessorNotFound() {
        when(professorRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Professor with id: 1 not found!");
    }

    @Test
    void shouldNotDelete_ProfessorHasAssignedLessons() {
        when(professorRepo.findById(1L)).thenReturn(Optional.of(new ProfessorEntity()));
        when(lessonRepo.findAll(ArgumentMatchers.<Specification<LessonEntity>>any()))
                .thenReturn(List.of(new LessonEntity()));

        assertThatThrownBy(() -> professorService.delete(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Can't delete professor. Professor has assigned lessons!");
    }

    @Test
    void shouldDeleteProfessor() {
        var professorEntity = getProfessorEntity();
        professorEntity.setId(1L);
        when(professorRepo.findById(professorEntity.getId())).thenReturn(Optional.of(professorEntity));

        professorService.delete(professorEntity.getId());

        verify(professorRepo).delete(professorEntity);
    }

    private static ProfessorDto getProfessorDto() {
        return ProfessorDto
                .builder()
                .firstName("John")
                .lastName("Snow")
                .email("john.snow@mail.com")
                .build();
    }

    private ProfessorEntity getProfessorEntity() {
        return ProfessorEntity
                .builder()
                .firstName("John")
                .lastName("Snow")
                .email("john.snow@mail.com")
                .build();
    }
}
