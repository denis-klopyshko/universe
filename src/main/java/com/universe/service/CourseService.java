package com.universe.service;

import com.universe.dto.course.CourseResponseDto;
import com.universe.dto.course.CreateCourseForm;
import com.universe.dto.course.EditCourseForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface CourseService {
    List<CourseResponseDto> findAll();

    Page<CourseResponseDto> findAll(Pageable pageable);

    CourseResponseDto create(@Valid @NotNull CreateCourseForm courseForm);

    CourseResponseDto update(@NotNull Long id, @Valid @NotNull EditCourseForm editCourseForm);

    CourseResponseDto findOne(@NotNull Long id);

    void delete(@NotNull Long courseId);
}
