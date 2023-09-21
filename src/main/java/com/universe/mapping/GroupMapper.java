package com.universe.mapping;

import com.universe.dto.group.CreateGroupForm;
import com.universe.dto.group.EditGroupForm;
import com.universe.dto.group.GroupResponseDto;
import com.universe.entity.GroupEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentShortMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupResponseDto mapToDto(GroupEntity groupEntity);

    EditGroupForm mapToEditForm(GroupResponseDto groupResponseDto);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    GroupEntity mapToEntity(CreateGroupForm createGroupForm);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    void updateGroupFromDto(EditGroupForm editGroupForm, @MappingTarget GroupEntity groupEntity);
}