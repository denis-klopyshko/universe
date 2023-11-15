package com.universe.dto.professor;

import com.universe.dto.RoleDto;
import com.universe.dto.course.CourseShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ProfessorDto {
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

    @NotNull
    private boolean enabled;

    @Builder.Default
    @NotEmpty
    private List<@NotNull @Valid CourseShortDto> courses = new ArrayList<>();

    @Builder.Default
    @NotEmpty
    private List<@NotNull @Valid RoleDto> roles = new ArrayList<>();

    public Set<String> getCourseNames() {
        return this.getCourses()
                .stream()
                .map(CourseShortDto::getName)
                .collect(Collectors.toSet());
    }
}
