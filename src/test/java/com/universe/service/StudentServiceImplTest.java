package com.universe.service;

import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.dto.student.StudentDto;
import com.universe.entity.CourseEntity;
import com.universe.entity.GroupEntity;
import com.universe.entity.StudentEntity;
import com.universe.exception.ResourceNotFoundException;
import com.universe.repository.CourseRepository;
import com.universe.repository.GroupRepository;
import com.universe.repository.StudentRepository;
import com.universe.repository.UserRepository;
import com.universe.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {StudentServiceImpl.class})
public class StudentServiceImplTest {
    @MockBean
    CourseRepository courseRepo;

    @MockBean
    GroupRepository groupRepo;

    @MockBean
    UserRepository userRepo;

    @MockBean
    StudentRepository studentRepo;

    @Autowired
    StudentServiceImpl studentService;

    @Test
    void shouldCreateNewStudentWithGroup() {
        var studentEntity = getStudentEntity();
        var courseEntity = CourseEntity.builder().id(1L).name("Math").description("Math Description").build();
        studentEntity.setCourses(List.of(courseEntity));
        var studentDto = getStudentDto();

        when(studentRepo.save(any(StudentEntity.class))).thenReturn(studentEntity);
        when(userRepo.existsByEmail(anyString())).thenReturn(false);
        when(groupRepo.findById(anyLong())).thenReturn(Optional.of(studentEntity.getGroup()));
        when(courseRepo.findAllByNameIn(anyList())).thenReturn(List.of(courseEntity));

        var createdStudent = studentService.create(studentDto);

        assertThat(createdStudent)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(studentDto);

        assertThat(createdStudent.getId()).isNotNull();
    }

    @Test
    void shouldCreateNewStudentWithoutGroup() {
        var studentEntity = getStudentEntity();
        studentEntity.setGroup(null);
        studentEntity.setId(1L);

        when(studentRepo.save(any(StudentEntity.class))).thenReturn(studentEntity);
        when(userRepo.existsByEmail(anyString())).thenReturn(false);
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());

        var studentDto = getStudentDto();
        studentDto.setGroup(null);
        studentDto.setCourses(Collections.emptyList());
        var createdStudent = studentService.create(studentDto);

        assertThat(createdStudent)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(studentDto);

        assertThat(createdStudent.getId()).isNotNull();
        assertThat(createdStudent.getGroup()).isNull();
    }

    @Test
    void shouldCreateStudent_IgnoreGroupWhenNotFound() {
        ArgumentCaptor<StudentEntity> arg1 = ArgumentCaptor.forClass(StudentEntity.class);

        when(userRepo.existsByEmail(anyString())).thenReturn(false);
        when(groupRepo.findById(1L)).thenReturn(Optional.empty());
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());

        var studentDto = getStudentDto();
        studentDto.setCourses(Collections.emptyList());

        studentService.create(studentDto);
        verify(studentRepo).save(arg1.capture());

        assertThat(arg1.getValue().getGroup()).isNull();
    }

    @Test
    void shouldNotDeleteStudent() {
        when(studentRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student with id: 1 not found!");
    }

    @Test
    void shouldDeleteStudent() {
        var studentEntity = getStudentEntity();
        studentEntity.setId(1L);
        when(studentRepo.findById(anyLong())).thenReturn(Optional.of(studentEntity));

        studentService.delete(studentEntity.getId());

        verify(studentRepo).delete(studentEntity);
    }

    private StudentEntity getStudentEntity() {
        var groupEntity = GroupEntity.builder().id(1L).build();
        return StudentEntity.builder()
                .id(1L)
                .group(groupEntity)
                .email("john.snow@mail.com")
                .firstName("John")
                .lastName("Snow")
                .build();
    }

    private StudentDto getStudentDto() {
        GroupShortDto groupDto = GroupShortDto.builder().id(1L).build();
        CourseShortDto courseDto = CourseShortDto.builder().id(1L).name("Math").description("Math Description").build();
        return StudentDto.builder()
                .group(groupDto)
                .email("john.snow@mail.com")
                .firstName("John")
                .lastName("Snow")
                .courses(List.of(courseDto))
                .build();
    }
}
