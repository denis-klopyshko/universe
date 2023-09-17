package com.universe.dto.course;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
public class CourseShortDto {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    private String description;

    public CourseShortDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
