package com.universe.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private static final String GROUP_NAME_PATTERN = "^[aA-zZ]{2}-\\d{2}";

    private Long id;

    @NotBlank
    @Pattern(regexp = GROUP_NAME_PATTERN, message = "Group name should match regexp.")
    private String name;

    private Integer studentsQuantity;
}
