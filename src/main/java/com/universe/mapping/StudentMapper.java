package com.universe.mapping;

import com.universe.dto.student.StudentDto;
import com.universe.dto.user.UserResponseDto;
import com.universe.entity.StudentEntity;
import com.universe.enums.UserType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {CourseShortMapper.class, RoleMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "enabled", source = "enabled")
    StudentDto mapToDto(StudentEntity studentEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "courses", ignore = true)
    StudentEntity mapBaseAttributes(StudentDto studentDto);

    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "group", ignore = true)
    void updateStudentFromDto(StudentDto studentDto, @MappingTarget StudentEntity studentEntity);

    @Mapping(target = "userType", expression = "java(getCurrentUserType())")
    @Mapping(target = "group.name", source = "group.name")
    @Mapping(target = "group.id", source = "group.id")
    UserResponseDto mapToUserResponse(StudentEntity studentEntity);

    default UserType getCurrentUserType() {
        return UserType.STUDENT;
    }

    default List<String> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}