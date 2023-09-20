package com.universe.mapping;

import com.universe.dto.course.CourseResponseDto;
import com.universe.dto.course.CreateCourseForm;
import com.universe.dto.course.EditCourseForm;
import com.universe.dto.professor.ProfessorShortDto;
import com.universe.dto.student.StudentShortDto;
import com.universe.entity.CourseEntity;
import com.universe.entity.ProfessorEntity;
import com.universe.entity.StudentEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseDtoMapperTest {
    private static final CourseMapper MAPPER = CourseMapper.INSTANCE;

    @Test
    void shouldMapEntityToResponseDto() {
        var student = StudentEntity.builder().id(1L).build();
        var professor = ProfessorEntity.builder().id(1L).build();
        var courseEntity = CourseEntity.builder()
                .id(1L)
                .name("Math")
                .description("Description")
                .students(List.of(student))
                .professors(List.of(professor))
                .build();

        CourseResponseDto courseDto = MAPPER.mapToDto(courseEntity);

        assertThat(courseDto.getId()).isEqualTo(courseEntity.getId());
        assertThat(courseDto.getName()).isEqualTo(courseEntity.getName());
        assertThat(courseDto.getDescription()).isEqualTo(courseEntity.getDescription());
        assertThat(courseDto.getStudents().get(0).getId()).isEqualTo(courseEntity.getStudents().get(0).getId());
        assertThat(courseDto.getProfessors().get(0).getId()).isEqualTo(courseEntity.getProfessors().get(0).getId());
    }

    @Test
    void shouldMapCreateFormToEntity() {
        var courseDto = CreateCourseForm.builder()
                .name("Math")
                .description("Description")
                .build();

        CourseEntity courseEntity = MAPPER.mapToEntity(courseDto);

        assertThat(courseEntity.getName()).isEqualTo(courseDto.getName());
        assertThat(courseEntity.getDescription()).isEqualTo(courseDto.getDescription());
        assertThat(courseEntity.getId()).isNull();
        assertThat(courseEntity.getStudents()).isEmpty();
        assertThat(courseEntity.getProfessors()).isEmpty();
    }

    @Test
    void shouldMapResponseDtoWithEditForm() {
        var courseResponseDto = CourseResponseDto.builder()
                .name("Math")
                .description("Description")
                .id(1L)
                .students(List.of(new StudentShortDto(1L, "John", "Doe", "john.doe@example.com")))
                .professors(List.of(new ProfessorShortDto(1L, "Ben", "Hodges", "ben.hodges@example.com")))
                .build();

        var editForm = MAPPER.mapDtoToEditForm(courseResponseDto);

        assertThat(editForm.getName()).isEqualTo(courseResponseDto.getName());
        assertThat(editForm.getDescription()).isEqualTo(courseResponseDto.getDescription());
        assertThat(editForm.getId()).isEqualTo(courseResponseDto.getId());
        assertThat(editForm.getStudentsEmails()).containsExactly("john.doe@example.com");
        assertThat(editForm.getProfessorsEmails()).containsExactly("ben.hodges@example.com");
    }

    @Test
    void shouldUpdateEntityWithEditFormDto() {
        var courseEditForm = EditCourseForm.builder()
                .id(1L)
                .name("Mathematics")
                .description("Mathematics Description")
                .studentsEmails(List.of("student1@example.com", "student2@example.com"))
                .professorsEmails(List.of("professor1@example.com", "professor2@example.com"))
                .build();

        var courseEntity = CourseEntity.builder()
                .id(1L)
                .name("Math")
                .description("Description")
                .build();

        MAPPER.updateEntityFromRequest(courseEditForm, courseEntity);

        assertThat(courseEntity.getName()).isEqualTo(courseEditForm.getName());
        assertThat(courseEntity.getId()).isEqualTo(courseEditForm.getId());
        assertThat(courseEntity.getDescription()).isEqualTo(courseEditForm.getDescription());
        assertThat(courseEntity.getStudents()).isEmpty();
        assertThat(courseEntity.getProfessors()).isEmpty();
    }
}