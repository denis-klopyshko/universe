package com.universe.rest;

import com.universe.dto.student.StudentDto;
import com.universe.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public Page<StudentDto> findAll(Pageable pageable) {
        return studentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public StudentDto getOneById(@PathVariable Long id) {
        return studentService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<StudentDto> create(@RequestBody StudentDto studentDto) {
        var createdStudent = studentService.create(studentDto);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdStudent.getId()).toUri();
        return ResponseEntity.created(location).body(createdStudent);
    }

    @PutMapping("/{id}")
    public StudentDto update(@PathVariable Long id, @RequestBody StudentDto studentDto) {
        if (!id.equals(studentDto.getId())) {
            throw new IllegalStateException("ID in path and body does not match!");
        }
        return studentService.update(id, studentDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
