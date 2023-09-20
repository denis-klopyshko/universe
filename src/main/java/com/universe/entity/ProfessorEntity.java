package com.universe.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("professor")
public class ProfessorEntity extends UserEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Builder.Default
    @ToString.Exclude
    private List<CourseEntity> courses = new ArrayList<>();

    public ProfessorEntity(UserEntity user) {
        super(user);
    }

    public String getFullName() {
        return String.format("%s %s", this.getFirstName(), this.getLastName());
    }

    public void addCourse(CourseEntity course) {
        this.courses.add(course);
        course.getProfessors().add(this);
    }

    public void removeCourse(CourseEntity course) {
        this.courses.remove(course);
        course.getProfessors().remove(this);
    }
}
