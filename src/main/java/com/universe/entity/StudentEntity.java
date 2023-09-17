package com.universe.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("student")
public class StudentEntity extends UserEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @ToString.Exclude
    private GroupEntity group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Builder.Default
    @ToString.Exclude
    private List<CourseEntity> courses = new ArrayList<>();

    public void addCourse(CourseEntity course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(CourseEntity course) {
        this.courses.remove(course);
        course.getStudents().remove(this);
    }

    public void addToGroup(GroupEntity group) {
        this.resetGroup();
        this.group = group;
        group.getStudents().add(this);
    }

    public void resetGroup() {
        if (this.group != null) {
            this.group.getStudents().remove(this);
            this.group = null;
        }
    }

    public StudentEntity(UserEntity user) {
        super(user);
        this.courses = new ArrayList<>();
    }

    public Set<String> getAssignedCourseNames() {
        return this.getCourses()
                .stream()
                .map(CourseEntity::getName)
                .collect(Collectors.toSet());
    }
}
