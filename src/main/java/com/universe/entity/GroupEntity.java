package com.universe.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "groups")
public class GroupEntity {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String name;

    @OneToMany(mappedBy = "group")
    @Builder.Default
    private List<StudentEntity> students = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    @Builder.Default
    private List<LessonEntity> lessons = new ArrayList<>();
}
