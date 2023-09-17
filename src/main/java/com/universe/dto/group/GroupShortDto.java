package com.universe.dto.group;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class GroupShortDto {
    private Long id;

    private String name;

    public GroupShortDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
