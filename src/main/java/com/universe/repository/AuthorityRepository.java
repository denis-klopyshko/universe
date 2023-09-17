package com.universe.repository;

import com.universe.dto.AuthorityDto;
import com.universe.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    @Query("select new com.universe.dto.AuthorityDto(a.id, a.name) from AuthorityEntity a")
    Optional<AuthorityDto> findByName(String authority);

    @Query("select distinct a from AuthorityEntity a join fetch a.roles r where r.name in ?1")
    List<AuthorityEntity> findAllByRoleNameIn(Set<String> roleName);
}

