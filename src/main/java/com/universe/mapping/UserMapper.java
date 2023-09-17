package com.universe.mapping;

import com.universe.dto.user.UserResponseDto;
import com.universe.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "userType", ignore = true)
    UserResponseDto mapUserResponseBaseAttributes(UserEntity userEntity);
}
