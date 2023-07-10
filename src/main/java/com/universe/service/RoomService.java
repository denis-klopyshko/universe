package com.universe.service;

import com.universe.dto.RoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface RoomService {
    Page<RoomDto> findAll(Pageable pageable);

    RoomDto create(@Valid @NotNull RoomDto roomDto);

    RoomDto update(@NotNull Long roomId, @Valid @NotNull RoomDto roomDto);

    RoomDto findById(@NotNull Long roomId);

    RoomDto findByCode(@NotNull String roomCode);

    void delete(@NotNull Long roomId);
}
