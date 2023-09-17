package com.universe.mapping;

import com.universe.dto.professor.ProfessorDto;
import com.universe.dto.RoleDto;
import com.universe.dto.user.UpdateUserForm;
import com.universe.dto.user.UserResponseDto;
import com.universe.entity.ProfessorEntity;
import com.universe.enums.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {RoleMapper.class})
public interface ProfessorMapper {
    ProfessorMapper INSTANCE = Mappers.getMapper(ProfessorMapper.class);

    ProfessorDto mapToDto(ProfessorEntity professor);

    @Mapping(target = "id", ignore = true)
    ProfessorEntity mapBaseAttributes(ProfessorDto professorDto);

    void updateProfessorFromDto(ProfessorDto professorDto, @MappingTarget ProfessorEntity professor);

    ProfessorDto mapUpdateUserRequestDtoToProfessorDto(UpdateUserForm updateUserRequest);

    @Mapping(target = "userType", expression = "java(getCurrentUserType())")
    UserResponseDto mapToUserResponse(ProfessorEntity professorEntity);

    default UserType getCurrentUserType() {
        return UserType.PROFESSOR;
    }

    default List<String> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    default List<RoleDto> map(Set<String> roleNames) {
        return roleNames.stream()
                .map(RoleDto::new)
                .collect(Collectors.toList());
    }
}
