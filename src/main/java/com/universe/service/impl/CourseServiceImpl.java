package com.universe.service.impl;

import com.universe.dto.course.CourseDto;
import com.universe.entity.CourseEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.CourseMapper;
import com.universe.repository.CourseRepository;
import com.universe.repository.LessonRepository;
import com.universe.rest.filter.CourseFilter;
import com.universe.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.universe.repository.LessonRepository.Specs.byCourseId;

@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private static final CourseMapper MAPPER = CourseMapper.INSTANCE;
    private final CourseRepository courseRepo;
    private final LessonRepository lessonRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDto> findAll(CourseFilter courseFilter, Pageable pageable) {
        return courseRepo.findAll(courseFilter.toSpec(), pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    public CourseDto create(CourseDto courseDto) {
        log.info("Creating new course: {}", courseDto);
        validateNameIsUnique(courseDto.getName());
        var course = MAPPER.mapToEntity(courseDto);

        return MAPPER.mapToDto(courseRepo.save(course));
    }

    @Override
    public CourseDto update(Long id, CourseDto courseDto) {
        log.info("Updating course: {}", courseDto);
        var courseEntity = findCourseEntity(id);
        if (!courseEntity.getName().equals(courseDto.getName())) {
            validateNameIsUnique(courseDto.getName());
        }

        MAPPER.updateCourseFromDto(courseDto, courseEntity);
        return MAPPER.mapToDto(courseRepo.save(courseEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto findOne(Long courseId) {
        var courseEntity = findCourseEntity(courseId);
        return MAPPER.mapToDto(courseEntity);
    }

    @Override
    public void delete(Long courseId) {
        log.info("Deleting course with ID [{}]", courseId);
        findCourseEntity(courseId);
        var courseLessons = lessonRepo.findAll(byCourseId(courseId));
        if (!courseLessons.isEmpty()) {
            throw new ConflictException("Can't delete course. Course has assigned lessons!");
        }

        courseRepo.deleteById(courseId);
    }

    private void validateNameIsUnique(String name) {
       if(courseRepo.existsByName(name)) {
           throw new ConflictException(String.format("Course with name '%s' already exists!", name));
       }
    }

    private CourseEntity findCourseEntity(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Course with id: %s not found!", id))
                );
    }
}
