package com.universe.dto.user;

import com.universe.enums.UserType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public abstract class BaseUserForm {
    @NotBlank
    @Size(min = 3, max = 100, message = "First Name length should be between 3 and 100 symbols.")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 100, message = "Last Name length should be between 3 and 100 symbols.")
    private String lastName;

    @Email(message = "Email not valid.")
    @NotNull
    private String email;

    @NotNull(message = "Please, select User Type.")
    private UserType userType;

    private String group;

    @Builder.Default
    private Set<@NotNull String> courses = new HashSet<>();

    @NotEmpty(message = "User should have at least one role.")
    @Builder.Default
    private Set<@NotNull String> roles = new HashSet<>();
}
