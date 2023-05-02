package com.universe.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class GroupEntity {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String name;

    @OneToMany(mappedBy = "group")
    private List<StudentEntity> students = new ArrayList<>();

    public void addStudent(StudentEntity studentEntity) {
        this.students.add(studentEntity);
        studentEntity.setGroup(this);
    }

    public void removeStudent(StudentEntity studentEntity) {
        this.students.remove(studentEntity);
        studentEntity.setGroup(null);
    }
}
