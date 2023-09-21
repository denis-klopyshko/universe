package com.universe.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CreateGroupForm {
    private static final String GROUP_NAME_PATTERN = "^[aA-zZ]{2}-\\d{2}$";

    @NotBlank
    @Pattern(regexp = GROUP_NAME_PATTERN, message = "Group name should match regexp: " + GROUP_NAME_PATTERN)
    private String name;
}
