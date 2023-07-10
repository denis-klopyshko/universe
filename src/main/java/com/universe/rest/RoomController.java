package com.universe.rest;

import com.universe.dto.RoomDto;
import com.universe.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public Page<RoomDto> findAll(Pageable pageable) {
        return roomService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public RoomDto getOneById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoomDto> create(@RequestBody RoomDto roomDto) {
        var createdRoom = roomService.create(roomDto);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRoom.getId()).toUri();

        return ResponseEntity.created(location).body(createdRoom);
    }

    @PutMapping("/{id}")
    public RoomDto update(@PathVariable Long id, @RequestBody RoomDto roomDto) {
        if (!id.equals(roomDto.getId())) {
            throw new IllegalStateException("ID in path and body does not match!");
        }
        return roomService.update(id, roomDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roomService.delete(id);
    }
}
