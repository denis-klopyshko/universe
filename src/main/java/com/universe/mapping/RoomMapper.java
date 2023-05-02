package com.universe.mapping;

import com.universe.dto.RoomDto;
import com.universe.entity.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomDto mapToDto(RoomEntity roomEntity);

    RoomEntity mapToEntity(RoomDto roomDto);

    void updateRoomFromDto(RoomDto roomDto, @MappingTarget RoomEntity roomEntity);
}
