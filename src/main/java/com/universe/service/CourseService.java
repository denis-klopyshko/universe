package com.universe.service;

import com.universe.dto.course.CourseDto;
import com.universe.rest.filter.CourseFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface CourseService {
    Page<CourseDto> findAll(CourseFilter courseFilter, Pageable pageable);

    CourseDto create(@Valid @NotNull CourseDto courseDto);

    CourseDto update(@NotNull Long id, @Valid @NotNull CourseDto courseDto);

    CourseDto findOne(@NotNull Long id);

    void delete(@NotNull Long courseId);
}
