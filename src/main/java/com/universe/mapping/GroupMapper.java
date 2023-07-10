package com.universe.mapping;

import com.universe.dto.group.GroupDto;
import com.universe.entity.GroupEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentShortMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupDto mapToDto(GroupEntity groupEntity);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    GroupEntity mapToEntity(GroupDto groupDto);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    void updateGroupFromDto(GroupDto groupDto, @MappingTarget GroupEntity groupEntity);
}