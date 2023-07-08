package com.universe.service;

import com.universe.dto.course.CourseDto;
import com.universe.entity.CourseEntity;
import com.universe.entity.LessonEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.repository.CourseRepository;
import com.universe.repository.LessonRepository;
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

    @Autowired
    CourseServiceImpl courseService;

    @Test
    void shouldCreateNewCourse() {
        CourseEntity course = getCourseEntity();

        when(courseRepo.existsByName(course.getName())).thenReturn(false);
        when(courseRepo.save(any(CourseEntity.class))).thenReturn(course);

        CourseDto newCourseDto = CourseDto.builder().name("Math").description("Math Description").build();
        CourseDto courseDto = courseService.create(newCourseDto);

        assertThat(courseDto)
                .usingRecursiveComparison()
                .isEqualTo(newCourseDto);

        verify(courseRepo).save(any(CourseEntity.class));
    }

    @Test
    void shouldNotCreateCourseAlreadyExists() {
        CourseEntity courseEntity = getCourseEntity();

        when(courseRepo.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> courseService.create(CourseDto.builder().name("Math").build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Course with name '%s' already exists!", courseEntity.getName());
    }

    @Test
    void shouldNotUpdateCourseNotFound() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.update(1L, new CourseDto()))
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

        CourseDto courseDto = courseService.update(1L, CourseDto.builder().id(1L).name("Biology").build());
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
