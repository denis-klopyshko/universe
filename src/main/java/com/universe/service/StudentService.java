package com.universe.service;

import com.universe.dto.student.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface StudentService {
    List<StudentDto> findAll();
    Page<StudentDto> findAll(Pageable pageable);

    Page<StudentDto> findAllByGroupId(@NotNull Long groupId, Pageable pageable);

    List<StudentDto> findAllByCourseName(@NotNull String courseName);

    StudentDto create(@Valid @NotNull StudentDto studentDto);

    StudentDto update(@NotNull Long id, @Valid @NotNull StudentDto studentDto);

    StudentDto findOne(@NotNull Long id);

    void delete(@NotNull Long studentId);

    void assignStudentOnCourse(@NotNull Long studentId, @NotNull Long courseId);

    void removeStudentFromCourse(@NotNull Long studentId, @NotNull Long courseId);
}
