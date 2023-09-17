package com.universe.mapping;

import com.universe.dto.RoleDto;
import com.universe.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto mapToDto(RoleEntity roleEntity);

    @Mapping(target = "authorities", ignore = true)
    RoleEntity mapToEntity(RoleDto roleDto);
}
