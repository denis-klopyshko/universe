package com.universe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("professor")
public class ProfessorEntity extends UserEntity {
    @Column(name = "user_type", insertable = false, updatable = false)
    private String userType;

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
        course.getProfessors().add(this);
    }

    public void removeCourse(CourseEntity course) {
        this.courses.remove(course);
        course.getProfessors().remove(this);
    }

    public String getFullName() {
        return String.format("%s %s", this.getFirstName(), this.getLastName());
    }
}
