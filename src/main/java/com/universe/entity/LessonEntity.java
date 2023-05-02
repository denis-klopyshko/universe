package com.universe.entity;

import com.universe.enums.LessonType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "lessons")
public class LessonEntity {
    @Id
    @Column(name = "lesson_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @OneToOne
    @JoinColumn(name = "user_id")
    private ProfessorEntity professor;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "lessons")
    @Builder.Default
    @ToString.Exclude
    private List<StudentEntity> students = new ArrayList<>();

    @Column(name = "lesson_order", nullable = false)
    private Integer order;

    @Column(name = "lesson_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonType type;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
}
