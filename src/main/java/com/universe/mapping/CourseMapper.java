package com.universe.mapping;

import com.universe.dto.course.CourseResponseDto;
import com.universe.dto.course.CreateCourseForm;
import com.universe.dto.course.EditCourseForm;
import com.universe.dto.professor.ProfessorShortDto;
import com.universe.dto.student.StudentShortDto;
import com.universe.entity.CourseEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper(uses = {StudentShortMapper.class, ProfessorShortMapper.class})
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseResponseDto mapToDto(CourseEntity courseEntity);

    CourseEntity mapToEntity(CreateCourseForm courseForm);

    EditCourseForm mapDtoToEditForm(CourseResponseDto courseResponseDto);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "professors", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(EditCourseForm editCourseForm, @MappingTarget CourseEntity courseEntity);

    @AfterMapping
    default void mapStudentsEmails(CourseResponseDto courseResponseDto, @MappingTarget EditCourseForm.EditCourseFormBuilder builder) {
        var studentsEmails = courseResponseDto.getStudents()
                .stream()
                .map(StudentShortDto::getEmail)
                .collect(Collectors.toList());
        builder.studentsEmails(studentsEmails);
    }

    @AfterMapping
    default void mapProfessorsEmails(CourseResponseDto courseResponseDto, @MappingTarget EditCourseForm.EditCourseFormBuilder builder) {
        var professorEmails = courseResponseDto.getProfessors()
                .stream()
                .map(ProfessorShortDto::getEmail)
                .collect(Collectors.toList());
        builder.professorsEmails(professorEmails);
    }
}
