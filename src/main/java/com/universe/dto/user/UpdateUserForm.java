package com.universe.dto.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class UpdateUserForm extends BaseUserForm {
    @NotNull
    private Long id;

    @NotNull
    private boolean enabled;
}
