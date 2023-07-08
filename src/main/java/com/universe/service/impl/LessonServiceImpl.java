package com.universe.service.impl;

import com.universe.dto.lesson.LessonDto;
import com.universe.entity.*;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.LessonMapper;
import com.universe.repository.*;
import com.universe.rest.filter.LessonFilter;
import com.universe.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private static final LessonMapper MAPPER = LessonMapper.INSTANCE;
    private final LessonRepository lessonRepo;

    private final CourseRepository courseRepo;

    private final ProfessorRepository professorRepo;

    private final GroupRepository groupRepo;

    private final RoomRepository roomRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<LessonDto> findAll(LessonFilter lessonFilter, Pageable pageable) {
        return lessonRepo.findAll(lessonFilter.toSpec(), pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    public LessonDto create(LessonDto lessonDto) {
        var professorEntity = findProfessorEntity(lessonDto.getProfessor().getId());
        var roomEntity = findRoomEntity(lessonDto.getRoom().getId());
        var courseEntity = findCourseEntity(lessonDto.getCourse().getId());
        var groupEntity = findGroupEntity(lessonDto.getGroup().getId());

        var lessonEntity = LessonEntity.builder()
                .professor(professorEntity)
                .course(courseEntity)
                .group(groupEntity)
                .room(roomEntity)
                .dayOfWeek(lessonDto.getDayOfWeek())
                .weekNumber(lessonDto.getWeekNumber())
                .order(lessonDto.getOrder())
                .type(lessonDto.getType())
                .build();

        try {
            var createdLesson = lessonRepo.save(lessonEntity);
            return MAPPER.mapToDto(createdLesson);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_unique")) {
                throw new ConflictException("Lesson for this course and group already exists in this date!");
            }

            if (e.getMessage().contains("professor_unique")) {
                throw new ConflictException("Lesson for this professor and course already exists in this date!");
            }

            throw e;
        }
    }

    @Override
    public LessonDto update(Long lessonId, LessonDto lessonDto) {
        var lessonEntity = findLessonEntity(lessonId);
        var professorEntity = findProfessorEntity(lessonDto.getProfessor().getId());
        var roomEntity = findRoomEntity(lessonDto.getRoom().getId());
        var courseEntity = findCourseEntity(lessonDto.getCourse().getId());
        var groupEntity = findGroupEntity(lessonDto.getGroup().getId());

        lessonEntity.setCourse(courseEntity);
        lessonEntity.setGroup(groupEntity);
        lessonEntity.setProfessor(professorEntity);
        lessonEntity.setRoom(roomEntity);
        lessonEntity.setDayOfWeek(lessonDto.getDayOfWeek());
        lessonEntity.setWeekNumber(lessonDto.getWeekNumber());
        lessonEntity.setOrder(lessonDto.getOrder());
        lessonEntity.setType(lessonDto.getType());

        try {
            var updatedLesson = lessonRepo.save(lessonEntity);
            return MAPPER.mapToDto(updatedLesson);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_unique")) {
                throw new ConflictException("Lesson for this course and group already exists in this date!");
            }

            if (e.getMessage().contains("professor_unique")) {
                throw new ConflictException("Lesson for this professor and course already exists in this date!");
            }

            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LessonDto findOne(Long lessonId) {
        var lessonEntity = findLessonEntity(lessonId);
        return MAPPER.mapToDto(lessonEntity);
    }

    @Override
    public void delete(Long lessonId) {
        var lessonEntity = findLessonEntity(lessonId);
        lessonRepo.delete(lessonEntity);
    }

    private LessonEntity findLessonEntity(Long lessonId) {
        return lessonRepo.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found by ID:" + lessonId));
    }

    private RoomEntity findRoomEntity(Long id) {
        return roomRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found by ID:" + id));
    }

    private ProfessorEntity findProfessorEntity(Long id) {
        return professorRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found by ID:" + id));
    }

    private CourseEntity findCourseEntity(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found by ID:" + id));
    }

    private GroupEntity findGroupEntity(Long id) {
        return groupRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found by ID:" + id));
    }
}
