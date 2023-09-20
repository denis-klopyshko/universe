package com.universe.repository;

import com.universe.entity.ProfessorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorEntity, Long> {
    Page<ProfessorEntity> findAll(Pageable pageable);

    List<ProfessorEntity> findAllByEmailIn(Collection<String> emails);
}
