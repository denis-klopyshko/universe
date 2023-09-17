package com.universe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorityDto {
    private Long id;
    private String name;

    public AuthorityDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
