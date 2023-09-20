package com.universe.dto.course;

import com.universe.dto.professor.ProfessorShortDto;
import com.universe.dto.student.StudentShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CourseResponseDto {
    private Long id;

    private String name;

    private String description;

    @Builder.Default
    private List<ProfessorShortDto> professors = new ArrayList<>();

    @Builder.Default
    private List<StudentShortDto> students = new ArrayList<>();

    public static CourseResponseDto ofId(Long courseId) {
        return CourseResponseDto.builder().id(courseId).build();
    }

    public List<String> getProfessorsFullNames() {
        return this.professors
                .stream()
                .map(ProfessorShortDto::getFullName)
                .collect(Collectors.toList());
    }

    public List<String> getStudentsFullNames() {
        return this.students
                .stream()
                .map(StudentShortDto::getFullName)
                .collect(Collectors.toList());
    }
}
