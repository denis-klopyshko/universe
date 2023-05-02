package com.universe.service.impl;

import com.universe.dto.course.CourseDto;
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
import com.universe.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final StudentMapper MAPPER = StudentMapper.INSTANCE;
    private final StudentRepository studentRepo;

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
        validateStudentExistsByEmail(studentDto.getEmail());
        log.info("Creating student: {}", studentDto);
        var studentEntity = MAPPER.mapBaseAttributes(studentDto);
        setStudentGroup(studentEntity, studentDto.getGroup());
        var createdStudent = studentRepo.save(studentEntity);

        if (!studentDto.getCourses().isEmpty()) {
            studentDto.getCourses()
                    .forEach(courseDto -> assignStudentOnCourse(createdStudent, courseDto.getId()));
        }

        return MAPPER.mapToDto(createdStudent);
    }

    @Override
    public StudentDto update(Long id, StudentDto studentDto) {
        log.info("Updating student with ID [{}]. Payload: {}", id, studentDto);
        var studentEntity = findStudentEntity(id);
        validateStudentExistsByEmail(studentDto.getEmail());
        setStudentGroup(studentEntity, studentDto.getGroup());

        MAPPER.updateStudentFromDto(studentDto, studentEntity);
        var updatedStudent = studentRepo.save(studentEntity);

        if (!studentDto.getCourses().isEmpty()) {
            studentEntity.getCourses().clear();
            studentDto.getCourses()
                    .forEach(courseDto -> assignStudentOnCourse(updatedStudent, courseDto.getId()));
        }

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
        findStudentEntity(studentId);
        studentRepo.deleteById(studentId);
    }

    @Override
    public void assignStudentOnCourse(Long studentId, Long courseId) {
        var studentEntity = findStudentEntity(studentId);
        assignStudentOnCourse(studentEntity, courseId);
    }

    @Override
    public void removeStudentFromCourse(Long studentId, Long courseId) {
        var studentEntity = findStudentEntity(studentId);
        removeStudentFromCourse(studentEntity, CourseDto.ofId(courseId));
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

    private void removeStudentFromCourse(StudentEntity studentEntity, CourseDto courseDto) {
        log.info("Removing student with ID [{}] from course: {}", studentEntity.getId(), courseDto);
        CourseEntity courseEntity = findCourseEntity(courseDto.getId());

        if (!studentEntity.getCourses().contains(courseEntity)) {
            log.error("Student with ID:[{}] NOT assigned on course with ID:[{}]", studentEntity.getId(), courseDto);
            throw new IllegalStateException(
                    format("Student with ID:[%s] NOT assigned on course with ID: [%s]", studentEntity.getId(), courseDto.getId())
            );
        }

        studentEntity.removeCourse(courseEntity);
    }

    private void setStudentGroup(StudentEntity studentEntity, GroupShortDto groupShortDto) {
        Optional.ofNullable(groupShortDto)
                .map(GroupShortDto::getId)
                .flatMap(groupRepo::findById)
                .ifPresent(group -> group.addStudent(studentEntity));
    }

    private StudentEntity findStudentEntity(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(format("Student with id: %s not found!", id))
                );
    }

    private void validateStudentExistsByEmail(String email) {
        studentRepo.findByEmail(email)
                .ifPresent(cp -> {
                    throw new ConflictException(format("Student with email: %s already exists!", email));
                });
    }

    private CourseEntity findCourseEntity(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(format("Course with id: %s not found!", id))
                );
    }
}
