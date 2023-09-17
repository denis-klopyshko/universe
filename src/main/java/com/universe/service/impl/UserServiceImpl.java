package com.universe.service.impl;

import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UpdateUserForm;
import com.universe.dto.user.UserResponseDto;
import com.universe.entity.ProfessorEntity;
import com.universe.entity.StudentEntity;
import com.universe.entity.UserEntity;
import com.universe.enums.UserType;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.repository.CourseRepository;
import com.universe.repository.GroupRepository;
import com.universe.repository.RoleRepository;
import com.universe.repository.UserRepository;
import com.universe.service.UserService;
import com.universe.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@RequiredArgsConstructor
@Slf4j
@Validated
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final GroupRepository groupRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userRepo.findAll()
                .stream()
                .map(UserResponseDto::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userRepo
                .findAll(pageable)
                .map(UserResponseDto::fromUser);
    }

    @Override
    public UserResponseDto create(CreateUserForm createUserForm) {
        validateUserExistsByEmail(createUserForm.getEmail());
        switch (createUserForm.getUserType().getValue()) {
            case "student":
                return saveAsStudent(createUserForm);
            case "professor":
                return saveAsProfessor(createUserForm);
            default:
                return saveAsUser(createUserForm);
        }
    }

    private UserResponseDto saveAsUser(CreateUserForm request) {
        log.info("{} adding new Admin User: {}", SecurityUtils.getCurrentUserLogin(), request);
        var userEntity = request.toUserEntity();
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setEnabled(true);

        setRoles(request.getRoles(), userEntity);

        return UserResponseDto.fromUser(userRepo.save(userEntity));
    }

    private UserResponseDto saveAsProfessor(CreateUserForm request) {
        log.info("{} adding new Professor User: {}", SecurityUtils.getCurrentUserLogin(), request);
        var professorEntity = new ProfessorEntity(request.toUserEntity());
        professorEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        professorEntity.setEnabled(true);

        setRoles(request.getRoles(), professorEntity);

        return UserResponseDto.fromUser(userRepo.save(professorEntity));
    }

    private UserResponseDto saveAsStudent(CreateUserForm request) {
        log.info("{} adding new Student User: {}", SecurityUtils.getCurrentUserLogin(), request);
        var studentEntity = new StudentEntity(request.toUserEntity());
        studentEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        studentEntity.setEnabled(true);
        Optional.ofNullable(request.getGroup())
                .flatMap(groupRepo::findByName)
                .ifPresent(studentEntity::addToGroup);

        if (!request.getCourses().isEmpty()) {
            courseRepo.findAllByNameIn(request.getCourses())
                    .forEach(studentEntity::addCourse);
        }

        setRoles(request.getRoles(), studentEntity);

        return UserResponseDto.fromUser(userRepo.save(studentEntity));
    }

    @Override
    public UserResponseDto update(Long id, UpdateUserForm updateUserRequest) {
        var user = findUserEntity(id);
        log.info("{} updating {} User: {}", SecurityUtils.getCurrentUserLogin(), user.getEmail(), updateUserRequest);
        if (!user.getEmail().equals(updateUserRequest.getEmail())) {
            validateUserExistsByEmail(updateUserRequest.getEmail());
        }

        return updateUser(user, updateUserRequest);
    }

    private UserResponseDto updateUser(UserEntity user, UpdateUserForm updateUserRequest) {
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setEmail(updateUserRequest.getEmail());
        user.setEnabled(updateUserRequest.isEnabled());
        user.getRoles().clear();
        setRoles(updateUserRequest.getRoles(), user);

        if (updateUserRequest.getUserType().equals(UserType.STUDENT)) {
            var studentUser = new StudentEntity(user);
            return updateStudentUser(studentUser, updateUserRequest);
        }

        if (updateUserRequest.getUserType().equals(UserType.PROFESSOR)) {
            var professorEntity = new ProfessorEntity(user);
            return UserResponseDto.fromUser(userRepo.save(professorEntity));
        }

        return UserResponseDto.fromUser(userRepo.save(user));
    }

    private UserResponseDto updateStudentUser(StudentEntity student, UpdateUserForm updateRequest) {
        Optional.ofNullable(updateRequest.getGroup())
                .flatMap(groupRepo::findByName)
                .ifPresentOrElse(
                        student::addToGroup,
                        student::resetGroup
                );

        if (updateRequest.getCourses().isEmpty()) {
            student.getCourses().clear();
        }

        if (!updateRequest.getCourses().isEmpty()) {
            var currentCourseNames = student.getAssignedCourseNames();
            var updatedCoursesNames = updateRequest.getCourses();

            var removedCourses = CollectionUtils.removeAll(currentCourseNames, updatedCoursesNames);
            var addedCourses = CollectionUtils.removeAll(updatedCoursesNames, currentCourseNames);

            if (isNotEmpty(addedCourses)) {
                courseRepo.findAllByNameIn(addedCourses)
                        .forEach(student::addCourse);
            }

            if (isNotEmpty(removedCourses)) {
                courseRepo.findAllByNameIn(removedCourses)
                        .forEach(student::removeCourse);
            }
        }

        return UserResponseDto.fromUser(userRepo.save(student));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findOne(Long id) {
        var user = findUserEntity(id);
        return UserResponseDto.fromUser(user);
    }

    @Override
    public void delete(Long id) {
        var user = findUserEntity(id);
        log.info("{} deleting User: {}", SecurityUtils.getCurrentUserLogin(), user.getEmail());
        userRepo.delete(user);
    }

    private UserEntity findUserEntity(Long id) {
        return userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with ID: %d not found", id))
        );
    }

    private void validateUserExistsByEmail(String email) {
        if (userRepo.existsByEmail(email)) {
            throw new ConflictException(format("User with email: %s already exists!", email));
        }
    }

    private void setRoles(Set<String> roleNames, UserEntity user) {
        if (!roleNames.isEmpty()) {
            roleRepo.findAllByNameIn(roleNames)
                    .forEach(user::addRole);
        }
    }
}
