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
@ToString
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_lessons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    @Builder.Default
    @ToString.Exclude
    private List<LessonEntity> lessons = new ArrayList<>();

    public void addCourse(CourseEntity course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(CourseEntity course) {
        this.courses.remove(course);
        course.getStudents().remove(this);
    }

    public void addLesson(LessonEntity lessonEntity) {
        this.lessons.add(lessonEntity);
        lessonEntity.getStudents().add(this);
    }

    public void removeLesson(LessonEntity lessonEntity) {
        this.lessons.remove(lessonEntity);
        lessonEntity.getStudents().remove(this);
    }
}
