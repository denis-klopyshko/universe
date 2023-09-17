package com.universe.service;

import com.universe.repository.CourseRepository;
import com.universe.repository.GroupRepository;
import com.universe.repository.ProfessorRepository;
import com.universe.repository.RoomRepository;
import com.universe.service.impl.LessonServiceImpl;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {LessonServiceImpl.class})
public class LessonServiceImplTest {

    @MockBean
    private GroupRepository groupRepo;

    @MockBean
    private CourseRepository courseRepo;

    @MockBean
    private ProfessorRepository professorRepo;

    @MockBean
    private RoomRepository roomRepo;

    @InjectMocks
    private LessonServiceImpl lessonService;
}
