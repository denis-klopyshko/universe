package com.universe.service;

import com.universe.dto.group.CreateGroupForm;
import com.universe.dto.group.EditGroupForm;
import com.universe.dto.group.GroupResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface GroupService {
    Page<GroupResponseDto> findAll(Pageable pageable);

    List<GroupResponseDto> findAll();

    List<GroupResponseDto> findAllWithLessOrEqualStudents(@NotNull Integer studentsQuantity);

    GroupResponseDto create(@Valid @NotNull CreateGroupForm createGroupForm);

    GroupResponseDto update(@NotNull Long id, @Valid @NotNull EditGroupForm editGroupForm);

    GroupResponseDto findOne(@NotNull Long groupId);

    void delete(@NotNull Long groupId);
}
