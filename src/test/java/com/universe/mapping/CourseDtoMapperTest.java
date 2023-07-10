package com.universe.mapping;

import com.universe.dto.course.CourseDto;
import com.universe.dto.student.StudentShortDto;
import com.universe.entity.CourseEntity;
import com.universe.entity.StudentEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseDtoMapperTest {
    private static final CourseMapper MAPPER = CourseMapper.INSTANCE;

    @Test
    void shouldMapEntityToDto() {
        var student = StudentEntity.builder().id(1L).build();
        var courseEntity = CourseEntity.builder()
                .id(1L)
                .name("Math")
                .description("Description")
                .students(List.of(student))
                .build();

        CourseDto courseDto = MAPPER.mapToDto(courseEntity);

        assertThat(courseDto.getId()).isEqualTo(courseEntity.getId());
        assertThat(courseDto.getName()).isEqualTo(courseEntity.getName());
        assertThat(courseDto.getDescription()).isEqualTo(courseEntity.getDescription());
        assertThat(courseDto.getStudents().get(0).getId()).isEqualTo(courseEntity.getStudents().get(0).getId());
    }

    @Test
    void shouldMapDtoToEntity() {
        var student = StudentShortDto.builder().id(1L).build();
        var courseDto = CourseDto.builder()
                .id(1L)
                .name("Math")
                .description("Description")
                .students(List.of(student))
                .build();

        CourseEntity courseEntity = MAPPER.mapToEntity(courseDto);

        assertThat(courseEntity.getName()).isEqualTo(courseDto.getName());
        assertThat(courseEntity.getId()).isEqualTo(courseDto.getId());
        assertThat(courseEntity.getDescription()).isEqualTo(courseDto.getDescription());
        assertThat(courseEntity.getStudents()).isEmpty();
    }

    @Test
    void shouldUpdateEntityWithDto() {
        var student = StudentShortDto.builder().id(1L).build();
        var courseDto = CourseDto.builder()
                .id(1L)
                .name("Biology")
                .description("Biology Description")
                .students(List.of(student))
                .build();
        var courseEntity = CourseEntity.builder().id(1L).name("Math").description("Description").build();

        MAPPER.updateCourseFromDto(courseDto, courseEntity);

        assertThat(courseEntity.getName()).isEqualTo(courseDto.getName());
        assertThat(courseEntity.getId()).isEqualTo(courseDto.getId());
        assertThat(courseEntity.getId()).isEqualTo(courseDto.getId());
        assertThat(courseEntity.getStudents()).isEmpty();
    }
}