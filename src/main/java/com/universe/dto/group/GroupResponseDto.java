package com.universe.dto.group;

import com.universe.dto.student.StudentShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponseDto {

    private Long id;

    private String name;

    @Builder.Default
    private List<StudentShortDto> students = new ArrayList<>();
}
