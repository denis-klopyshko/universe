package com.universe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RoomDto {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 5, message = "Room Code size should be from 2 to 5 symbols.")
    private String code;
}
