package com.universe.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum UserType {
    STUDENT("student"),
    PROFESSOR("professor"),
    ADMIN("admin");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(UserType.values())
                .map(UserType::getValue)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }
}
