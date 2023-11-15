package com.universe.mapping;

import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.dto.student.StudentDto;
import com.universe.entity.CourseEntity;
import com.universe.entity.GroupEntity;
import com.universe.entity.StudentEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudentDtoMapperTest {
    private static final StudentMapper MAPPER = StudentMapper.INSTANCE;

    @Test
    void shouldMapEntityToDto() {
        GroupEntity group = GroupEntity.builder().id(1L).build();
        CourseEntity course = CourseEntity.builder().id(1L).name("Math").description("Description").build();
        StudentEntity studentEntity = StudentEntity.builder()
                .id(1L)
                .group(group)
                .firstName("John")
                .lastName("Snow")
                .enabled(true)
                .courses(List.of(course))
                .build();

        StudentDto studentDto = MAPPER.mapToDto(studentEntity);

        assertThat(studentDto)
                .usingRecursiveComparison()
                .isEqualTo(studentEntity);
    }

    @Test
    void shouldMapBaseAttributesFromDtoToEntity() {
        GroupShortDto groupDto = GroupShortDto.builder().id(1L).build();
        CourseShortDto courseDto = CourseShortDto.builder().id(1L).name("Math").description("Description").build();
        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .group(groupDto)
                .firstName("John")
                .lastName("Snow")
                .courses(List.of(courseDto))
                .build();

        StudentEntity studentEntity = MAPPER.mapBaseAttributes(studentDto);

        assertThat(studentEntity.getGroup()).isNull();
        assertThat(studentEntity.getCourses()).isEmpty();

        assertThat(studentEntity.getId()).isNull();
        assertThat(studentEntity.getFirstName()).isEqualTo(studentDto.getFirstName());
        assertThat(studentEntity.getLastName()).isEqualTo(studentDto.getLastName());
    }

    @Test
    void shouldUpdateEntityWithDto() {
        CourseShortDto courseDto = CourseShortDto.builder().id(1L).name("Math").description("Description").build();
        List<CourseShortDto> courses = new ArrayList<>();
        courses.add(courseDto);
        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Snow")
                .courses(courses)
                .build();

        StudentEntity studentEntity = StudentEntity.builder()
                .id(1L)
                .group(GroupEntity.builder().id(2L).build())
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();


        MAPPER.updateStudentFromDto(studentDto, studentEntity);

        assertThat(studentEntity.getGroup().getId()).isEqualTo(2L);
        assertThat(studentEntity.getCourses()).isEmpty();

        assertThat(studentEntity.getId()).isEqualTo(1L);
        assertThat(studentEntity.getFirstName()).isEqualTo(studentDto.getFirstName());
        assertThat(studentEntity.getLastName()).isEqualTo(studentDto.getLastName());
    }
}
