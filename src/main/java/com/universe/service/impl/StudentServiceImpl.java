package com.universe.service.impl;

import com.universe.dto.student.StudentDto;
import com.universe.entity.StudentEntity;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.StudentMapper;
import com.universe.repository.*;
import com.universe.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
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

    private final UserRepository userRepository;

    private final GroupRepository groupRepo;

    private final CourseRepository courseRepo;

    private final RoleRepository roleRepository;

    @Override
    public List<StudentDto> findAll() {
        return studentRepo.findAll()
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

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
    @Transactional(readOnly = true)
    public StudentDto findOne(Long id) {
        var studentEntity = findStudentEntity(id);
        return MAPPER.mapToDto(studentEntity);
    }

    private StudentEntity findStudentEntity(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(format("Student with id: %s not found!", id))
                );
    }
}
