package com.universe.repository;

import com.universe.entity.GroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    Page<GroupEntity> findAll(Pageable pageable);

    Optional<GroupEntity> findByName(String name);

    @Query("select g from GroupEntity g where size(g.students)<= ?1")
    List<GroupEntity> findAllByStudentsIsLessThanOrEqual(int studentLimit);

    boolean existsByName(String name);
}
