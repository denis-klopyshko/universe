package com.universe.dto.group;

import com.universe.dto.student.StudentShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private static final String GROUP_NAME_PATTERN = "^[aA-zZ]{2}-\\d{2}$";

    private Long id;

    @NotBlank
    @Pattern(regexp = GROUP_NAME_PATTERN, message = "Group name should match regexp.")
    private String name;

    @Builder.Default
    private List<StudentShortDto> students = new ArrayList<>();
}
