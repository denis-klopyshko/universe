package com.universe.dto.group;

import com.universe.dto.student.StudentShortDto;
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
public class EditGroupForm extends CreateGroupForm {

    @NotNull
    private Long id;

    @Builder.Default
    private List<StudentShortDto> students = new ArrayList<>();
}
