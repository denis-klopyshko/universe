package com.universe.service.impl;

import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.dto.student.StudentDto;
import com.universe.entity.CourseEntity;
import com.universe.entity.StudentEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.StudentMapper;
import com.universe.repository.CourseRepository;
import com.universe.repository.GroupRepository;
import com.universe.repository.StudentRepository;
import com.universe.repository.UserRepository;
import com.universe.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.universe.repository.StudentRepository.Specs.withGroupId;
import static java.lang.String.format;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final StudentMapper MAPPER = StudentMapper.INSTANCE;

    private final StudentRepository studentRepo;

    private final UserRepository userRepository;

    private final GroupRepository groupRepo;

    private final CourseRepository courseRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDto> findAll(Pageable pageable) {
        return studentRepo.findAll(pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDto> findAllByGroupId(Long groupId, Pageable pageable) {
        return studentRepo.findAll(withGroupId(groupId), pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAllByCourseName(String courseName) {
        return studentRepo.findAllByCourseName(courseName)
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto create(StudentDto studentDto) {
        validateUserExistsByEmail(studentDto.getEmail());
        log.info("Creating student: {}", studentDto);
        var studentEntity = MAPPER.mapBaseAttributes(studentDto);

        Optional.ofNullable(studentDto.getGroup())
                .map(GroupShortDto::getId)
                .flatMap(groupRepo::findById)
                .ifPresent(studentEntity::addToGroup);

        if (!studentDto.getCourses().isEmpty()) {
            var courseNames = studentDto.getCourses()
                    .stream().map(CourseShortDto::getName)
                    .collect(Collectors.toSet());
            courseRepo.findAllByNameIn(courseNames)
                    .forEach(studentEntity::addCourse);
        }

        var createdStudent = studentRepo.save(studentEntity);
        return MAPPER.mapToDto(createdStudent);
    }

    @Override
    public StudentDto update(Long id, StudentDto updateRequest) {
        log.info("Updating student with ID [{}]. Payload: {}", id, updateRequest);
        var studentEntity = findStudentEntity(id);
        if (!studentEntity.getEmail().equals(updateRequest.getEmail())) {
            validateUserExistsByEmail(updateRequest.getEmail());
        }

        Optional.ofNullable(updateRequest.getGroup())
                .map(GroupShortDto::getId)
                .flatMap(groupRepo::findById)
                .ifPresentOrElse(
                        studentEntity::addToGroup,
                        studentEntity::resetGroup
                );

        if (!updateRequest.getCourses().isEmpty()) {
            var currentCourseNames = studentEntity.getCourses()
                    .stream()
                    .map(CourseEntity::getName)
                    .collect(Collectors.toList());

            var updatedCoursesNames = updateRequest.getCourses()
                    .stream()
                    .map(CourseShortDto::getName)
                    .collect(Collectors.toList());

            var removedCourses = CollectionUtils.removeAll(currentCourseNames, updatedCoursesNames);
            var addedCourses = CollectionUtils.removeAll(updatedCoursesNames, currentCourseNames);

            if (isNotEmpty(addedCourses)) {
                courseRepo.findAllByNameIn(addedCourses)
                        .forEach(studentEntity::addCourse);
            }

            if (isNotEmpty(removedCourses)) {
                courseRepo.findAllByNameIn(removedCourses)
                        .forEach(studentEntity::removeCourse);
            }
        }

        MAPPER.updateStudentFromDto(updateRequest, studentEntity);
        var updatedStudent = studentRepo.save(studentEntity);

        return MAPPER.mapToDto(updatedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto findOne(Long id) {
        var studentEntity = findStudentEntity(id);
        return MAPPER.mapToDto(studentEntity);
    }

    @Override
    public void delete(Long studentId) {
        log.info("Deleting student with id: [{}]", studentId);
        var studentEntity = findStudentEntity(studentId);
        studentRepo.delete(studentEntity);
    }

    @Override
    public void assignStudentOnCourse(Long studentId, Long courseId) {
        var studentEntity = findStudentEntity(studentId);
        assignStudentOnCourse(studentEntity, courseId);
    }

    @Override
    public void removeStudentFromCourse(Long studentId, Long courseId) {
        var studentEntity = findStudentEntity(studentId);
        removeStudentFromCourse(studentEntity, courseId);
    }

    private void assignStudentOnCourse(StudentEntity studentEntity, Long courseId) {
        log.info("Assigning student with ID [{}] on course: {}", studentEntity.getId(), courseId);
        CourseEntity courseEntity = findCourseEntity(courseId);

        if (studentEntity.getCourses().contains(courseEntity)) {
            log.error("Student with ID:[{}] already assigned on course with ID:[{}]", studentEntity.getId(), courseId);
            throw new ConflictException(
                    format("Student with ID:[%s] already assigned on course with ID: [%s]", studentEntity.getId(), courseId)
            );
        }

        studentEntity.addCourse(courseEntity);
    }

    private void removeStudentFromCourse(StudentEntity studentEntity, Long courseId) {
        log.info("Removing student with ID [{}] from course: {}", studentEntity.getId(), courseId);
        CourseEntity courseEntity = findCourseEntity(courseId);

        if (!studentEntity.getCourses().contains(courseEntity)) {
            log.error("Student with ID:[{}] NOT assigned on course with ID:[{}]", studentEntity.getId(), courseId);
            throw new IllegalStateException(
                    format("Student with ID:[%s] NOT assigned on course with ID: [%s]", studentEntity.getId(), courseId)
            );
        }

        studentEntity.removeCourse(courseEntity);
    }

    private StudentEntity findStudentEntity(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(format("Student with id: %s not found!", id))
                );
    }

    private void validateUserExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(format("User with email: %s already exists!", email));
        }
    }

    private CourseEntity findCourseEntity(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(format("Course with id: %s not found!", id))
                );
    }
}
