package com.universe.dto.user;

import com.universe.dto.AuthorityDto;
import com.universe.dto.RoleDto;
import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.entity.ProfessorEntity;
import com.universe.entity.StudentEntity;
import com.universe.entity.UserEntity;
import com.universe.enums.UserType;
import com.universe.mapping.CourseShortMapper;
import com.universe.mapping.UserMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
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
        var userResponseDto = UserMapper.INSTANCE.mapToResponseDto(user);

        if (userResponseDto.getUserType().equals(UserType.STUDENT)) {
            var userStudent = (StudentEntity) user;
            Optional.ofNullable(userStudent.getGroup())
                    .map(group -> new GroupShortDto(group.getId(), group.getName()))
                    .ifPresent(userResponseDto::setGroup);

            var userCourses = userStudent.getCourses().stream()
                    .map(CourseShortMapper.INSTANCE::mapToDto)
                    .collect(Collectors.toList());
            userResponseDto.setCourses(userCourses);
        }

        if (userResponseDto.getUserType().equals(UserType.PROFESSOR)) {
            var userProfessor = (ProfessorEntity) user;

            var userCourses = userProfessor.getCourses().stream()
                    .map(CourseShortMapper.INSTANCE::mapToDto)
                    .collect(Collectors.toList());
            userResponseDto.setCourses(userCourses);
        }

        return userResponseDto;
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
