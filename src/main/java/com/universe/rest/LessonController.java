package com.universe.rest;

import com.universe.dto.lesson.LessonDto;
import com.universe.rest.filter.LessonFilter;
import com.universe.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public Page<LessonDto> findAll(LessonFilter lessonFilter, Pageable pageable) {
        return lessonService.findAll(lessonFilter, pageable);
    }

    @GetMapping("/{id}")
    public LessonDto getOneById(@PathVariable Long id) {
        return lessonService.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDto create(@RequestBody LessonDto lessonDto) {
        return lessonService.create(lessonDto);
    }

    @PutMapping("/{id}")
    public LessonDto update(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        if (!id.equals(lessonDto.getId())) {
            throw new IllegalStateException("ID in path and body does not match!");
        }
        return lessonService.update(id, lessonDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        lessonService.delete(id);
    }
}
