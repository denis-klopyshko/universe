package com.universe.repository;

import com.universe.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query("select r.name from RoleEntity r")
    List<String> findAllRoleNames();

    Optional<RoleEntity> findByName(String roleName);

    List<RoleEntity> findAllByNameIn(Set<String> names);
}
