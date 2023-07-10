package com.universe.service.impl;

import com.universe.dto.RoomDto;
import com.universe.entity.RoomEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.mapping.RoomMapper;
import com.universe.repository.LessonRepository;
import com.universe.repository.RoomRepository;
import com.universe.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.universe.repository.LessonRepository.Specs.byRoomId;

@Service
@Slf4j
@Validated
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private static final RoomMapper MAPPER = RoomMapper.INSTANCE;
    private final RoomRepository roomRepo;

    private final LessonRepository lessonRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> findAll(Pageable pageable) {
        return roomRepo.findAll(pageable)
                .map(MAPPER::mapToDto);
    }

    @Override
    public RoomDto create(RoomDto roomDto) {
        log.info("Creating new room: {}", roomDto);
        validateCodeIsUnique(roomDto.getCode());
        var roomEntity = MAPPER.mapToEntity(roomDto);

        return MAPPER.mapToDto(roomRepo.save(roomEntity));
    }

    @Override
    public RoomDto update(Long roomId, RoomDto roomDto) {
        log.info("Updating room: {}", roomDto);
        var roomEntity = findRoomEntityById(roomId);
        if (!roomEntity.getCode().equals(roomDto.getCode())) {
            validateCodeIsUnique(roomDto.getCode());
        }

        MAPPER.updateRoomFromDto(roomDto, roomEntity);
        return MAPPER.mapToDto(roomRepo.save(roomEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto findById(Long roomId) {
        var roomEntity = findRoomEntityById(roomId);
        return MAPPER.mapToDto(roomEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto findByCode(String roomCode) {
        var roomEntity = roomRepo.findByCode(roomCode).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Room with code: %s not found!", roomCode))
        );
        return MAPPER.mapToDto(roomEntity);
    }

    @Override
    public void delete(Long roomId) {
        var lessonsInRoom = lessonRepository.findAll(byRoomId(roomId));
        if (!lessonsInRoom.isEmpty()) {
            throw new ConflictException("Can't delete Room. Room has assigned lessons!");
        }

        roomRepo.delete(findRoomEntityById(roomId));
    }

    private RoomEntity findRoomEntityById(Long id) {
        return roomRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Room with id: %s not found!", id))
                );
    }

    private void validateCodeIsUnique(String code) {
        if (roomRepo.existsByCode(code)) {
            throw new ConflictException(String.format("Room with code '%s' already exists!", code));
        }
    }
}
