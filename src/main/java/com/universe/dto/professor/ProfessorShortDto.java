package com.universe.dto.professor;

import com.universe.dto.course.CourseShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ProfessorShortDto {

    @NotNull
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private List<CourseShortDto> courses = new ArrayList<>();

    public ProfessorShortDto(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFullName() {
        return String.format("%s %s", this.getFirstName(), this.getLastName());
    }
}
