package com.universe.service.impl;

import com.universe.dto.course.CourseResponseDto;
import com.universe.dto.course.CreateCourseForm;
import com.universe.dto.course.EditCourseForm;
import com.universe.entity.CourseEntity;
import com.universe.entity.ProfessorEntity;
import com.universe.entity.StudentEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.CourseMapper;
import com.universe.repository.CourseRepository;
import com.universe.repository.LessonRepository;
import com.universe.repository.ProfessorRepository;
import com.universe.repository.StudentRepository;
import com.universe.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

import static com.universe.repository.LessonRepository.Specs.byCourseId;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private static final CourseMapper MAPPER = CourseMapper.INSTANCE;
    private final CourseRepository courseRepo;
    private final LessonRepository lessonRepo;
    private final StudentRepository studentRepo;
    private final ProfessorRepository professorRepo;

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponseDto> findAll() {
        return courseRepo.findAll()
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponseDto> findAll(Pageable pageable) {
        return courseRepo.findAll(pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    public CourseResponseDto create(CreateCourseForm courseForm) {
        log.info("Creating new course: {}", courseForm);
        validateNameIsUnique(courseForm.getName());
        var course = MAPPER.mapToEntity(courseForm);

        return MAPPER.mapToDto(courseRepo.save(course));
    }

    @Override
    public CourseResponseDto update(Long id, EditCourseForm editCourseForm) {
        log.info("Updating course: {}", editCourseForm);
        var courseEntity = findCourseEntity(id);
        if (!courseEntity.getName().equals(editCourseForm.getName())) {
            validateNameIsUnique(editCourseForm.getName());
        }
        MAPPER.updateEntityFromRequest(editCourseForm, courseEntity);

        updateProfessors(courseEntity, editCourseForm);
        updateStudents(courseEntity, editCourseForm);

        return MAPPER.mapToDto(courseRepo.save(courseEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponseDto findOne(Long courseId) {
        var courseEntity = findCourseEntity(courseId);
        return MAPPER.mapToDto(courseEntity);
    }

    @Override
    public void delete(Long courseId) throws ConflictException {
        log.info("Deleting course with ID [{}]", courseId);
        findCourseEntity(courseId);
        var courseLessons = lessonRepo.findAll(byCourseId(courseId));
        if (!courseLessons.isEmpty()) {
            throw new ConflictException("Can't delete course. Course has assigned lessons!");
        }

        courseRepo.deleteById(courseId);
    }

    private void validateNameIsUnique(String name) {
        if (courseRepo.existsByName(name)) {
            throw new ConflictException(String.format("Course with name '%s' already exists!", name));
        }
    }

    private CourseEntity findCourseEntity(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Course with id: %s not found!", id))
                );
    }

    private void updateProfessors(CourseEntity courseEntity, EditCourseForm editCourseForm) {
        if (!editCourseForm.getProfessorsEmails().isEmpty()) {
            var currentProfessorEmails = courseEntity.getProfessors()
                    .stream()
                    .map(ProfessorEntity::getEmail)
                    .collect(Collectors.toSet());

            var updatedProfessorEmails = editCourseForm.getProfessorsEmails();

            var removedProfessors = CollectionUtils.removeAll(currentProfessorEmails, updatedProfessorEmails);
            var addedProfessors = CollectionUtils.removeAll(updatedProfessorEmails, currentProfessorEmails);

            if (isNotEmpty(addedProfessors)) {
                professorRepo.findAllByEmailIn(addedProfessors)
                        .forEach(professorEntity -> professorEntity.addCourse(courseEntity));
            }

            if (isNotEmpty(removedProfessors)) {
                professorRepo.findAllByEmailIn(removedProfessors)
                        .forEach(professorEntity -> professorEntity.removeCourse(courseEntity));
            }
        }
    }

    private void updateStudents(CourseEntity courseEntity, EditCourseForm editCourseForm) {
        if (!editCourseForm.getStudentsEmails().isEmpty()) {
            var currentStudentEmails = courseEntity.getStudents()
                    .stream()
                    .map(StudentEntity::getEmail)
                    .collect(Collectors.toSet());

            var updatedStudentsEmails = editCourseForm.getStudentsEmails();

            var removedStudents = CollectionUtils.removeAll(currentStudentEmails, updatedStudentsEmails);
            var addedStudents = CollectionUtils.removeAll(updatedStudentsEmails, currentStudentEmails);

            if (isNotEmpty(addedStudents)) {
                studentRepo.findAllByEmailIn(addedStudents)
                        .forEach(studentEntity -> studentEntity.addCourse(courseEntity));
            }

            if (isNotEmpty(removedStudents)) {
                studentRepo.findAllByEmailIn(removedStudents)
                        .forEach(studentEntity -> studentEntity.removeCourse(courseEntity));
            }
        }
    }
}
