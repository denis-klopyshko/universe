package com.universe.mapping;

import com.universe.dto.AuthorityDto;
import com.universe.dto.user.UserResponseDto;
import com.universe.entity.RoleEntity;
import com.universe.entity.UserEntity;
import com.universe.enums.UserType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "userType", ignore = true)
    UserResponseDto mapToResponseDto(UserEntity userEntity);

    @AfterMapping
    default void mapAuthorities(UserEntity userEntity, @MappingTarget UserResponseDto.UserResponseDtoBuilder userResponseDto) {
        var userAuthorities = userEntity.getRoles().stream()
                .map(RoleEntity::getAuthorities)
                .flatMap(Collection::stream)
                .map(authorityEntity -> new AuthorityDto(authorityEntity.getId(), authorityEntity.getName()))
                .collect(Collectors.toList());
        userResponseDto.authorities(userAuthorities);
    }

    @AfterMapping
    default void mapUserType(UserEntity userEntity, @MappingTarget UserResponseDto.UserResponseDtoBuilder userResponseDto) {
        var userType = Optional.ofNullable(userEntity.getUserType())
                .map(type -> UserType.valueOf(userEntity.getUserType().toUpperCase()))
                .orElse(UserType.ADMIN);
        userResponseDto.userType(userType);
    }
}
