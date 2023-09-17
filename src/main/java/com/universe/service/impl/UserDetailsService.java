package com.universe.service.impl;

import com.universe.entity.RoleEntity;
import com.universe.entity.UserEntity;
import com.universe.repository.AuthorityRepository;
import com.universe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = userRepository.findByEmailFetchRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity user) {
        var roleNames = user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet());
        return authorityRepository
                .findAllByRoleNameIn(roleNames)
                .stream()
                .map(authorityEntity -> new SimpleGrantedAuthority(authorityEntity.getName()))
                .collect(Collectors.toList());
    }
}
