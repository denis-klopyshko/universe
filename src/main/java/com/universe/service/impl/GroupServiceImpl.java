package com.universe.service.impl;

import com.universe.dto.group.GroupDto;
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
    public Page<GroupDto> findAll(Pageable pageable) {
        return groupRepo.findAll(pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> findAllWithLessOrEqualStudents(Integer studentsQuantity) {
        return groupRepo.findAllByStudentsIsLessThanOrEqual(studentsQuantity)
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto create(GroupDto groupDto) {
        log.info("Creating new group: {}", groupDto);
        validateNameIsUnique(groupDto.getName());
        GroupEntity savedGroupEntity = groupRepo.save(MAPPER.mapToEntity(groupDto));
        return MAPPER.mapToDto(savedGroupEntity);
    }

    @Override
    public GroupDto update(Long id, GroupDto groupDto) {
        log.info("Updating group: {}", groupDto);
        GroupEntity groupEntity = findGroupEntity(id);
        if (!groupEntity.getName().equals(groupDto.getName())) {
            validateNameIsUnique(groupDto.getName());
        }

        MAPPER.updateGroupFromDto(groupDto, groupEntity);
        return MAPPER.mapToDto(groupRepo.save(groupEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDto findOne(Long groupId) {
        GroupEntity groupEntity = findGroupEntity(groupId);
        return MAPPER.mapToDto(groupEntity);
    }

    @Override
    public void delete(Long groupId) {
        log.info("Deleting group with ID: {}", groupId);
        findGroupEntity(groupId);
        validateNoAssignedStudents(groupId);
        groupRepo.deleteById(groupId);
    }

    private void validateNameIsUnique(String name) {
        groupRepo.findByName(name)
                .ifPresent(cp -> {
                    throw new ConflictException(String.format("Group with name '%s' already exists!", name));
                });
    }

    private GroupEntity findGroupEntity(Long id) {
        return groupRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Group with id: %s not found!", id))
                );
    }

    private void validateNoAssignedStudents(Long groupId) {
        var group = findGroupEntity(groupId);
        if (!group.getStudents().isEmpty()) {
            throw new RelationRemovalException(
                    String.format("Can't delete group with assigned students! Students: %s", group.getStudents())
            );
        }
    }
}
