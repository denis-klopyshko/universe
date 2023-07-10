package com.universe.service;

import com.universe.dto.group.GroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface GroupService {
    Page<GroupDto> findAll(Pageable pageable);

    List<GroupDto> findAllWithLessOrEqualStudents(@NotNull Integer studentsQuantity);

    GroupDto create(@Valid @NotNull GroupDto groupDto);

    GroupDto update(@NotNull Long id, @Valid @NotNull GroupDto groupDto);

    GroupDto findOne(@NotNull Long groupId);

    void delete(@NotNull Long groupId);
}
