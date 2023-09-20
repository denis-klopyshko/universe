package com.universe.service;

import com.universe.dto.course.CourseResponseDto;
import com.universe.dto.course.CreateCourseForm;
import com.universe.dto.course.EditCourseForm;
import com.universe.entity.CourseEntity;
import com.universe.entity.LessonEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.repository.CourseRepository;
import com.universe.repository.LessonRepository;
import com.universe.repository.ProfessorRepository;
import com.universe.repository.StudentRepository;
import com.universe.service.impl.CourseServiceImpl;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CourseServiceImpl.class})
class CourseServiceImplTest {
    @MockBean
    CourseRepository courseRepo;

    @MockBean
    LessonRepository lessonRepo;

    @MockBean
    StudentRepository studentRepo;

    @MockBean
    ProfessorRepository professorRep;

    @Autowired
    CourseServiceImpl courseService;

    @Test
    void shouldCreateNewCourse() {
        CourseEntity course = getCourseEntity();

        when(courseRepo.existsByName(course.getName())).thenReturn(false);
        when(courseRepo.save(any(CourseEntity.class))).thenReturn(course);

        CreateCourseForm newCourseDto = CreateCourseForm.builder().name("Math").description("Math Description").build();
        CourseResponseDto courseDto = courseService.create(newCourseDto);

        assertThat(courseDto.getName()).isEqualTo(newCourseDto.getName());
        assertThat(courseDto.getDescription()).isEqualTo(newCourseDto.getDescription());

        verify(courseRepo).save(any(CourseEntity.class));
    }

    @Test
    void shouldNotCreateCourseAlreadyExists() {
        CourseEntity courseEntity = getCourseEntity();

        when(courseRepo.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> courseService.create(CreateCourseForm.builder().name("Math").build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Course with name '%s' already exists!", courseEntity.getName());
    }

    @Test
    void shouldNotUpdateCourseNotFound() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.update(1L, new EditCourseForm()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Course with id: 1 not found!");
    }

    @Test
    void shouldUpdateCourseNameAndDescription() {
        CourseEntity courseBeforeUpdate = getCourseEntity();
        CourseEntity courseAfterUpdate = getCourseEntity();
        courseAfterUpdate.setName("Biology");
        courseAfterUpdate.setDescription("Biology Description");

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(courseBeforeUpdate));
        when(courseRepo.save(any(CourseEntity.class))).thenReturn(courseAfterUpdate);

        CourseResponseDto courseDto = courseService.update(1L, EditCourseForm.builder().id(1L).name("Biology").build());
        assertThat(courseDto.getName()).isEqualTo("Biology");
        assertThat(courseDto.getDescription()).isEqualTo("Biology Description");
    }

    @Test
    void shouldFailToDelete_CourseNotFound() {
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Course with id: 1 not found!");
    }

    @Test
    void shouldFailToDelete_CourseHasAssignedCourses() {
        when(courseRepo.findById(1L)).thenReturn(Optional.of(new CourseEntity()));
        when(lessonRepo.findAll(ArgumentMatchers.<Specification<LessonEntity>>any()))
                .thenReturn(List.of(new LessonEntity()));

        assertThatThrownBy(() -> courseService.delete(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Can't delete course. Course has assigned lessons!");
    }


    @Test
    void shouldDeleteCourse() {
        CourseEntity course = getCourseEntity();
        when(courseRepo.findById(course.getId())).thenReturn(Optional.of(course));

        courseService.delete(course.getId());

        verify(courseRepo).deleteById(course.getId());
    }

    private CourseEntity getCourseEntity() {
        return CourseEntity.builder()
                .name("Math")
                .description("Math Description")
                .build();
    }
}
