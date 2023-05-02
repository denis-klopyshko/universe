package com.universe.dto.course;

import com.universe.dto.professor.ProfessorShortDto;
import com.universe.dto.student.StudentShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CourseDto {
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String description;

    @Builder.Default
    private List<@NotNull ProfessorShortDto> professors = new ArrayList<>();

    @Builder.Default
    private List<@NotNull StudentShortDto> students = new ArrayList<>();

    public static CourseDto ofId(Long courseId) {
        return CourseDto.builder().id(courseId).build();
    }
}
