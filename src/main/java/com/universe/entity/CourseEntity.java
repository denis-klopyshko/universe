package com.universe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@Table(name = "courses")
public class CourseEntity {
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_description", nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @Where(clause = "user_type='professor'")
    @Builder.Default
    @ToString.Exclude
    private List<ProfessorEntity> professors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @Where(clause = "user_type='student'")
    @Builder.Default
    @ToString.Exclude
    private List<StudentEntity> students = new ArrayList<>();
}
