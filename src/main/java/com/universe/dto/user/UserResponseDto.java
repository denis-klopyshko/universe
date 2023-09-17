package com.universe.dto.user;

import com.universe.dto.AuthorityDto;
import com.universe.dto.RoleDto;
import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.entity.RoleEntity;
import com.universe.entity.StudentEntity;
import com.universe.entity.UserEntity;
import com.universe.enums.UserType;
import com.universe.mapping.CourseShortMapper;
import com.universe.mapping.RoleMapper;
import com.universe.mapping.UserMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponseDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private boolean enabled;

    private GroupShortDto group;

    private UserType userType;

    @Builder.Default
    private List<CourseShortDto> courses = new ArrayList<>();

    @Builder.Default
    private List<RoleDto> roles = new ArrayList<>();

    @Builder.Default
    private List<AuthorityDto> authorities = new ArrayList<>();

    public static UserResponseDto fromUser(UserEntity user) {
        var response = UserMapper.INSTANCE.mapUserResponseBaseAttributes(user);
        var userRoles = user.getRoles().stream()
                .map(RoleMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
        response.setRoles(userRoles);
        var userType = Optional.ofNullable(user.getUserType())
                .map(type -> UserType.valueOf(user.getUserType().toUpperCase()))
                .orElse(UserType.ADMIN);
        response.setUserType(userType);

        var userAuthorities = user.getRoles().stream()
                .map(RoleEntity::getAuthorities)
                .flatMap(Collection::stream)
                .map(authorityEntity -> new AuthorityDto(authorityEntity.getId(), authorityEntity.getName()))
                .collect(Collectors.toList());
        response.setAuthorities(userAuthorities);

        if (user.getUserType() != null && user.getUserType().equals(UserType.STUDENT.getValue())) {
            var userStudent = (StudentEntity) user;
            Optional.ofNullable(userStudent.getGroup())
                    .map(group -> new GroupShortDto(group.getId(), group.getName()))
                    .ifPresent(response::setGroup);

            var userCourses = userStudent.getCourses().stream()
                    .map(CourseShortMapper.INSTANCE::mapToDto)
                    .collect(Collectors.toList());
            response.setCourses(userCourses);
        }

        return response;
    }

    public UpdateUserForm toUpdateForm() {
        var courseNames = this.courses.stream().map(CourseShortDto::getName).collect(Collectors.toSet());
        var roleNames = this.roles.stream().map(RoleDto::getName).collect(Collectors.toSet());
        var groupName = Optional.ofNullable(this.getGroup()).map(GroupShortDto::getName).orElse(null);
        return UpdateUserForm.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .userType(this.userType)
                .enabled(this.enabled)
                .courses(courseNames)
                .group(groupName)
                .roles(roleNames)
                .build();
    }
}
