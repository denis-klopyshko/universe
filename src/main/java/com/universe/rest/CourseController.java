package com.universe.rest;

import com.universe.dto.course.CourseDto;
import com.universe.dto.professor.ProfessorShortDto;
import com.universe.dto.student.StudentShortDto;
import com.universe.rest.filter.CourseFilter;
import com.universe.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public Page<CourseDto> findAll(CourseFilter filter, Pageable pageable) {
        return courseService.findAll(filter, pageable);
    }

    @GetMapping("/{id}")
    public CourseDto getOneById(@PathVariable Long id) {
        return courseService.findOne(id);
    }

    @GetMapping("/{id}/students")
    public List<StudentShortDto> getStudentsByCourseId(@PathVariable Long id) {
        var course = courseService.findOne(id);
        return course.getStudents();
    }

    @GetMapping("/{id}/professors")
    public List<ProfessorShortDto> getProfessorsByCourseId(@PathVariable Long id) {
        var course = courseService.findOne(id);
        return course.getProfessors();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto create(@RequestBody CourseDto courseDto) {
        return courseService.create(courseDto);
    }

    @PutMapping("/{id}")
    public CourseDto update(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        return courseService.update(id, courseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
