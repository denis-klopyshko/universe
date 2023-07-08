package com.universe.validation;

import com.universe.dto.group.GroupShortDto;
import com.universe.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class GroupExistsValidator implements ConstraintValidator<GroupExists, GroupShortDto> {
    private final GroupRepository groupRepository;

    @Override
    public boolean isValid(GroupShortDto groupShortDto, ConstraintValidatorContext ctx) {
        return Objects.isNull(groupShortDto) || groupRepository.existsById(groupShortDto.getId());
    }
}
