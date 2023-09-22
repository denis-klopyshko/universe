package com.universe.mapping;


import com.universe.dto.group.CreateGroupForm;
import com.universe.dto.group.EditGroupForm;
import com.universe.entity.GroupEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GroupDtoMapperTest {
    private static final GroupMapper MAPPER = GroupMapper.INSTANCE;

    @Test
    void shouldMapEntityToDto() {
        var groupEntity = GroupEntity.builder()
                .id(1L)
                .name("GR-11").build();

        var groupResponseDto = MAPPER.mapToDto(groupEntity);

        assertThat(groupResponseDto.getId()).isEqualTo(groupEntity.getId());
        assertThat(groupResponseDto.getName()).isEqualTo(groupEntity.getName());
    }

    @Test
    void shouldMapDtoToEntity() {
        CreateGroupForm createGroupForm = CreateGroupForm.builder().name("GR-11").build();

        GroupEntity groupEntity = MAPPER.mapToEntity(createGroupForm);

        assertThat(groupEntity.getName()).isEqualTo(createGroupForm.getName());
        assertThat(groupEntity.getId()).isNull();
    }

    @Test
    void shouldUpdateEntityWithDto() {
        EditGroupForm editGroupForm = EditGroupForm.builder().id(1L).name("GR-11").build();
        GroupEntity groupEntity = GroupEntity.builder().id(1L).name("GR-10").build();

        MAPPER.updateGroupFromDto(editGroupForm, groupEntity);

        assertThat(groupEntity.getName()).isEqualTo(editGroupForm.getName());
        assertThat(groupEntity.getId()).isEqualTo(editGroupForm.getId());
    }
}