package com.universe.service;

import com.universe.dto.course.CourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface CourseService {
    Page<CourseDto> findAll(Pageable pageable);

    List<CourseDto> findAllByStudentId(@NotNull Long studentId);

    CourseDto create(@Valid @NotNull CourseDto courseDto);

    CourseDto update(@NotNull Long id, @Valid @NotNull CourseDto courseDto);

    CourseDto findOne(@NotNull Long id);

    void delete(@NotNull Long courseId);
}
