package com.universe.service.impl;

import com.universe.dto.group.CreateGroupForm;
import com.universe.dto.group.EditGroupForm;
import com.universe.dto.group.GroupResponseDto;
import com.universe.entity.GroupEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.RelationRemovalException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.GroupMapper;
import com.universe.repository.GroupRepository;
import com.universe.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private static final GroupMapper MAPPER = GroupMapper.INSTANCE;

    private final GroupRepository groupRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<GroupResponseDto> findAll(Pageable pageable) {
        return groupRepo.findAll(pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponseDto> findAll() {
        return groupRepo.findAll()
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponseDto> findAllWithLessOrEqualStudents(Integer studentsQuantity) {
        return groupRepo.findAllByStudentsIsLessThanOrEqual(studentsQuantity)
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupResponseDto create(CreateGroupForm createGroupForm) {
        log.info("Creating new group: {}", createGroupForm);
        validateNameIsUnique(createGroupForm.getName());
        GroupEntity savedGroupEntity = groupRepo.save(MAPPER.mapToEntity(createGroupForm));
        return MAPPER.mapToDto(savedGroupEntity);
    }

    @Override
    public GroupResponseDto update(Long id, EditGroupForm editGroupForm) {
        log.info("Updating group: {}", editGroupForm);
        GroupEntity groupEntity = findGroupEntity(id);
        if (!groupEntity.getName().equals(editGroupForm.getName())) {
            validateNameIsUnique(editGroupForm.getName());
        }

        MAPPER.updateGroupFromDto(editGroupForm, groupEntity);
        return MAPPER.mapToDto(groupRepo.save(groupEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public GroupResponseDto findOne(Long groupId) {
        GroupEntity groupEntity = findGroupEntity(groupId);
        return MAPPER.mapToDto(groupEntity);
    }

    @Override
    public void delete(Long groupId) {
        log.info("Deleting group with ID: {}", groupId);
        var groupEntity = findGroupEntity(groupId);
        validateNoAssignedStudents(groupEntity);
        validateNoAssignedLessons(groupEntity);

        groupRepo.deleteById(groupId);
    }

    private void validateNameIsUnique(String name) {
        if (groupRepo.existsByName(name)) {
            throw new ConflictException(String.format("Group with name '%s' already exists!", name));
        }
    }

    private GroupEntity findGroupEntity(Long id) {
        return groupRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Group with id: %s not found!", id))
                );
    }

    private void validateNoAssignedStudents(GroupEntity groupEntity) {
        if (!groupEntity.getStudents().isEmpty()) {
            throw new RelationRemovalException("Can't delete group with assigned students!");
        }
    }

    private void validateNoAssignedLessons(GroupEntity groupEntity) {
        if (!groupEntity.getLessons().isEmpty()) {
            throw new RelationRemovalException("Can't delete group. Group has assigned lessons!");
        }
    }
}
