package com.universe.service;

import com.universe.dto.lesson.LessonDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface LessonService {
    List<LessonDto> findAll();

    Page<LessonDto> findAll(Pageable pageable);

    LessonDto create(@Valid @NotNull LessonDto lessonDto);

    LessonDto update(@NotNull Long lessonId, @Valid @NotNull LessonDto lessonDto);

    LessonDto findOne(@NotNull Long lessonId);

    void delete(@NotNull Long lessonId);
}
