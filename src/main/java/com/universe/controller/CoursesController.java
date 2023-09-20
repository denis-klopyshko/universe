package com.universe.controller;

import com.universe.dto.course.CreateCourseForm;
import com.universe.dto.course.EditCourseForm;
import com.universe.mapping.CourseMapper;
import com.universe.service.CourseService;
import com.universe.service.ProfessorService;
import com.universe.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CoursesController {
    private final CourseService courseService;
    private final StudentService studentService;
    private final ProfessorService professorService;

    @PreAuthorize("hasAnyAuthority({'courses::write', 'courses::read'})")
    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    public String getCoursesListPage(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "courses/courses-list";
    }

    @PreAuthorize("hasAnyAuthority('courses::write')")
    @RequestMapping(value = "/courses/new", method = RequestMethod.GET)
    public String getCreateCoursePage(Model model) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new CreateCourseForm());
        }

        return "courses/create-course";
    }

    @PreAuthorize("hasAnyAuthority('courses::write')")
    @RequestMapping(value = "/courses", method = RequestMethod.POST)
    public String createCourse(@Valid @ModelAttribute("course") CreateCourseForm course,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", result);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/courses/new";
        }

        try {
            courseService.create(course);
        } catch (Exception e) {
            log.error("Error creating course: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/courses/new";
        }

        redirectAttributes.addFlashAttribute("toastMessage",
                String.format("Course [ %s ] successfully created!", course.getName()));
        return "redirect:/courses";
    }

    @PreAuthorize("hasAnyAuthority('courses::write')")
    @RequestMapping(value = "/courses/{id}/edit", method = RequestMethod.GET)
    public String getEditCoursePage(Model model, @PathVariable("id") Long id) {
        var course = courseService.findOne(id);
        var editCourseForm = CourseMapper.INSTANCE.mapDtoToEditForm(course);

        model.addAttribute("students", studentService.findAll());
        model.addAttribute("professors", professorService.findAll());

        if (!model.containsAttribute("course")) {
            model.addAttribute("course", editCourseForm);
        }

        return "courses/edit-course";
    }

    @PreAuthorize("hasAnyAuthority('courses::write')")
    @RequestMapping(value = "/courses/{id}/edit", method = RequestMethod.POST)
    public String updateCourse(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("course") EditCourseForm course,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", result);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/courses/{id}/edit";
        }

        try {
            courseService.update(id, course);
        } catch (Exception e) {
            log.error("Error updating course: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/courses/{id}/edit";
        }

        return "redirect:/courses";
    }

    @PreAuthorize("hasAnyAuthority('courses::write')")
    @RequestMapping(value = "/courses/{id}/delete", method = RequestMethod.GET)
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.delete(id);
        } catch (Exception e) {
            log.error("Error deleting course: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("toastError", e.getLocalizedMessage());
            return "redirect:/courses";
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Course successfully deleted!");
        return "redirect:/courses";
    }
}
