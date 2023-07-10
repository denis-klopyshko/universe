package com.universe.dto.professor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

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
}
