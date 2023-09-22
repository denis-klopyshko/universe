package com.universe.service;

import com.universe.dto.group.CreateGroupForm;
import com.universe.dto.group.EditGroupForm;
import com.universe.dto.group.GroupResponseDto;
import com.universe.entity.GroupEntity;
import com.universe.entity.LessonEntity;
import com.universe.entity.StudentEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.RelationRemovalException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.repository.GroupRepository;
import com.universe.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {
    @MockBean
    private GroupRepository groupRepo;

    @Autowired
    private GroupServiceImpl groupService;

    @Test
    void shouldCreateNewGroup() {
        GroupEntity group = getGroupEntity();
        var createGroupForm = CreateGroupForm.builder().name("GR-10").build();

        when(groupRepo.existsByName(createGroupForm.getName())).thenReturn(false);
        when(groupRepo.save(any(GroupEntity.class))).thenReturn(group);

        var createdGroup = groupService.create(createGroupForm);
        assertThat(createdGroup.getName()).isEqualTo("GR-10");
        assertThat(createdGroup.getId()).isEqualTo(1L);

        verify(groupRepo).save(any(GroupEntity.class));
    }

    @Test
    void shouldNotCreateGroupAlreadyExists() {
        var groupEntity = getGroupEntity();
        var createGroupForm = CreateGroupForm.builder().name("GR-10").build();

        when(groupRepo.existsByName(createGroupForm.getName())).thenReturn(true);

        assertThatThrownBy(() -> groupService.create(createGroupForm))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Group with name '%s' already exists!", groupEntity.getName());
    }

    @Test
    void shouldNotUpdateGroupNotFound() {
        when(groupRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.update(1L, EditGroupForm.builder().id(1L).name("GR-10").build()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Group with id: 1 not found!");
    }

    @Test
    void shouldUpdateGroupName() {
        GroupEntity groupBeforeUpdate = getGroupEntity();
        GroupEntity groupAfterUpdate = getGroupEntity();
        groupAfterUpdate.setName("GR-11");

        when(groupRepo.findById(groupBeforeUpdate.getId())).thenReturn(Optional.of(groupBeforeUpdate));
        when(groupRepo.save(any(GroupEntity.class))).thenReturn(groupAfterUpdate);

        GroupResponseDto groupDto = groupService.update(1L, EditGroupForm.builder().id(1L).name("GR-11").build());
        assertThat(groupDto.getName()).isEqualTo("GR-11");
    }

    @Test
    void shouldFailToDelete_groupNotFound() {
        when(groupRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Group with id: 1 not found!");
    }

    @Test
    void shouldFailToDelete_assignedStudents() {
        var groupEntity = getGroupEntity();
        groupEntity.setStudents(List.of(new StudentEntity()));
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));

        assertThatThrownBy(() -> groupService.delete(1L))
                .isInstanceOf(RelationRemovalException.class)
                .hasMessageContaining("Can't delete group with assigned students!");
    }

    @Test
    void shouldFailToDelete_assignedLessons() {
        var groupEntity = getGroupEntity();
        groupEntity.setLessons(List.of(new LessonEntity()));
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));

        assertThatThrownBy(() -> groupService.delete(1L))
                .isInstanceOf(RelationRemovalException.class)
                .hasMessageContaining("Can't delete group. Group has assigned lessons!");
    }

    @Test
    void shouldDeleteGroup() {
        GroupEntity group = getGroupEntity();
        when(groupRepo.findById(1L)).thenReturn(Optional.of(group));

        groupService.delete(1L);

        verify(groupRepo).deleteById(1L);
    }

    private GroupEntity getGroupEntity() {
        return GroupEntity.builder().id(1L).name("GR-10").build();
    }
}
