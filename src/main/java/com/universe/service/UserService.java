package com.universe.service;

import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UpdateUserForm;
import com.universe.dto.user.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();

    Page<UserResponseDto> findAll(Pageable pageable);

    UserResponseDto findByEmail(String email);

    UserResponseDto create(@Valid @NotNull CreateUserForm createUserForm);

    UserResponseDto update(@NotNull Long id, @Valid @NotNull UpdateUserForm updateUserForm);

    UserResponseDto findOne(@NotNull Long id);

    void delete(@NotNull Long id);
}
