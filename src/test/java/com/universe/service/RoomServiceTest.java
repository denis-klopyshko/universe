package com.universe.service;

import com.universe.dto.RoomDto;
import com.universe.entity.LessonEntity;
import com.universe.entity.RoomEntity;
import com.universe.exception.ConflictException;
import com.universe.exception.ResourceNotFoundException;
import com.universe.repository.LessonRepository;
import com.universe.repository.RoomRepository;
import com.universe.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RoomServiceImpl.class})
public class RoomServiceTest {
    @MockBean
    LessonRepository lessonRepo;

    @MockBean
    RoomRepository roomRepo;

    @Autowired
    RoomServiceImpl roomService;

    @Test
    void shouldCreateNewRoom() {
        var roomEntity = getRoomEntity();
        roomEntity.setId(1L);

        when(roomRepo.existsByCode(roomEntity.getCode())).thenReturn(false);
        when(roomRepo.save(any(RoomEntity.class))).thenReturn(roomEntity);

        var roomDto = RoomDto.builder().code("R101").build();
        var createdRoom = roomService.create(roomDto);

        assertThat(roomDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(createdRoom);

        verify(roomRepo).save(any(RoomEntity.class));
    }

    @Test
    void shouldNotCreate_RoomAlreadyExists() {
        var roomEntity = getRoomEntity();
        var roomDto = RoomDto.builder().code("R101").build();

        when(roomRepo.existsByCode(anyString())).thenReturn(true);

        assertThatThrownBy(() -> roomService.create(roomDto))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Room with code '%s' already exists!", roomEntity.getCode());
    }

    @Test
    void shouldNotUpdate_RoomNotFound() {
        when(roomRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.update(1L, new RoomDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Room with id: 1 not found!");
    }

    @Test
    void shouldUpdate_RoomCode() {
        var roomBeforeUpdate = getRoomEntity();
        var roomAfterUpdate = getRoomEntity();
        roomAfterUpdate.setCode("R102");

        when(roomRepo.findById(anyLong())).thenReturn(Optional.of(roomBeforeUpdate));
        when(roomRepo.save(any(RoomEntity.class))).thenReturn(roomAfterUpdate);

        var roomDto = roomService.update(1L, new RoomDto());
        assertThat(roomDto.getCode()).isEqualTo("R102");
    }

    @Test
    void shouldFailToDelete_RoomNotFound() {
        when(roomRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Room with id: 1 not found!");
    }

    @Test
    void shouldFailToDelete_CourseHasAssignedCourses() {
        when(roomRepo.findById(1L)).thenReturn(Optional.of(new RoomEntity()));
        when(lessonRepo.findAll(ArgumentMatchers.<Specification<LessonEntity>>any()))
                .thenReturn(List.of(new LessonEntity()));

        assertThatThrownBy(() -> roomService.delete(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Can't delete Room. Room has assigned lessons!");
    }

    @Test
    void shouldDeleteCourse() {
        var roomEntity = getRoomEntity();
        roomEntity.setId(1L);
        when(roomRepo.findById(roomEntity.getId())).thenReturn(Optional.of(roomEntity));

        roomService.delete(roomEntity.getId());

        verify(roomRepo).delete(roomEntity);
    }

    private RoomEntity getRoomEntity() {
        return RoomEntity.builder()
                .code("R101")
                .build();
    }
}
