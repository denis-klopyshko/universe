package com.universe.repository;

import com.universe.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("select distinct u from users u left join fetch u.roles r where u.email = ?1")
    Optional<UserEntity> findByEmailFetchRoles(String email);

    boolean existsByEmail(String email);
}
