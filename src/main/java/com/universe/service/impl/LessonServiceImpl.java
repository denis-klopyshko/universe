package com.universe.service.impl;

import com.universe.dto.lesson.LessonDto;
import com.universe.entity.CourseEntity;
import com.universe.entity.LessonEntity;
import com.universe.entity.ProfessorEntity;
import com.universe.entity.RoomEntity;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.LessonMapper;
import com.universe.repository.CourseRepository;
import com.universe.repository.LessonRepository;
import com.universe.repository.ProfessorRepository;
import com.universe.repository.RoomRepository;
import com.universe.rest.filter.LessonFilter;
import com.universe.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.DayOfWeek;

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

        var lessonEntity = new LessonEntity();
        lessonEntity.setProfessor(professorEntity);
        lessonEntity.setRoom(roomEntity);
        lessonEntity.setCourse(courseEntity);
        lessonEntity.setDayOfWeek(DayOfWeek.of(lessonDto.getDayOfWeek()));
        lessonEntity.setWeekNumber(lessonDto.getWeekNumber());
        lessonEntity.setOrder(lessonDto.getOrder());
        lessonEntity.setType(lessonDto.getType());

        var createdLesson = lessonRepo.save(lessonEntity);
        return MAPPER.mapToDto(createdLesson);
    }

    @Override
    public LessonDto update(Long lessonId, LessonDto lessonDto) {
        return null;
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
}
