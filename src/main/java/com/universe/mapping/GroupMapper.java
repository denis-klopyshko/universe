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

    @Mapping(target = "studentsQuantity", expression = "java(groupEntity.getStudents().size())")
    GroupDto mapToDto(GroupEntity groupEntity);

    GroupEntity mapToEntity(GroupDto groupDto);

    void updateGroupFromDto(GroupDto groupDto, @MappingTarget GroupEntity groupEntity);
}