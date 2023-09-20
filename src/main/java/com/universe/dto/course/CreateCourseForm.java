package com.universe.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CreateCourseForm {
    @NotBlank
    @Size(min = 3, max = 50, message = "Course Name length should be between 3 and 50 symbols.")
    private String name;

    @NotBlank
    @Size(min = 3, max = 255, message = "Course Description length should be between 3 and 255 symbols.")
    private String description;
}
