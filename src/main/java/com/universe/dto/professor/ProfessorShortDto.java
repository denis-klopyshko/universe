package com.universe.dto.professor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ProfessorShortDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;
}
