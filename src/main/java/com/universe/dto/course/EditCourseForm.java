package com.universe.dto.course;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class EditCourseForm extends CreateCourseForm {
    @NotNull
    private Long id;

    @Builder.Default
    private List<@NotNull String> studentsEmails = new ArrayList<>();

    @Builder.Default
    private List<@NotNull String> professorsEmails = new ArrayList<>();
}
