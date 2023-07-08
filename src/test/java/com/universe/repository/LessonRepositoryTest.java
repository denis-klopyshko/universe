package com.universe.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.DayOfWeek;

import static com.universe.repository.LessonRepository.Specs.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SqlGroup({
        // @Sql(scripts = "classpath:sql/test-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/clean.sql", executionPhase = AFTER_TEST_METHOD)
})
@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {LessonRepository.class}
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LessonRepositoryTest {

    @Autowired
    LessonRepository lessonRepo;

    @Test
    void shouldReturnListByWeekNumber() {
        var lessons = lessonRepo.findAll(byWeekNumber(4));
        assertThat(lessons).hasSize(3);
    }

    @Test
    void shouldReturnListByDayOfWeek() {
        var lessons = lessonRepo.findAll(byDayOfWeek(DayOfWeek.MONDAY));
        assertThat(lessons).hasSize(2);
    }

    @Test
    void shouldReturnListByOrder() {
        var lessons = lessonRepo.findAll(byOrder(2));
        assertThat(lessons).hasSize(3);
    }

    @Test
    void shouldReturnListByCourseName() {
        var lessons = lessonRepo.findAll(byCourseName("Mathematics"));
        assertThat(lessons).hasSize(4);
    }

    @Test
    void shouldReturnListByGroupId() {
        var lessons = lessonRepo.findAll(byGroupId(1L));
        assertThat(lessons).hasSize(4);
    }

    @Test
    void shouldReturnListByProfessorId() {
        var lessons = lessonRepo.findAll(byProfessorId(5L));
        assertThat(lessons).hasSize(1);
    }
}
