package com.universe.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.dto.lesson.LessonShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDto {
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @Email(message = "Email not valid.")
    @NotNull
    private String email;

    private GroupShortDto group;

    @Builder.Default
    private List<@NotNull CourseShortDto> courses = new ArrayList<>();

    @Builder.Default
    private List<@NotNull LessonShortDto> lessons = new ArrayList<>();
}