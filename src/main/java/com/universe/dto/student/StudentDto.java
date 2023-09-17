package com.universe.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.universe.dto.RoleDto;
import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
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

    @Valid
    private GroupShortDto group;

    @Builder.Default
    private List<@NotNull CourseShortDto> courses = new ArrayList<>();

    @NotEmpty
    @Builder.Default
    private List<@NotNull @Valid RoleDto> roles = new ArrayList<>();

    public Set<String> getCourseNames() {
        return this.getCourses()
                .stream()
                .map(CourseShortDto::getName)
                .collect(Collectors.toSet());
    }

    public Set<String> getRoleNames() {
        return this.getRoles()
                .stream()
                .map(RoleDto::getName)
                .collect(Collectors.toSet());
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}