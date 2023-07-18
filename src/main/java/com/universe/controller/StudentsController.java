package com.universe.controller;

import com.universe.dto.student.StudentDto;
import com.universe.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class StudentsController {
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE = 1;

    private final StudentService studentService;

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String getStudentsList(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(DEFAULT_PAGE);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<StudentDto> studentsPage = studentService.findAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("students", studentsPage);
        model.addAttribute("content", "students");
        System.out.println(studentsPage.getTotalElements());

        int totalPages = studentsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "index";
    }

    @RequestMapping(value = "/students/create", method = RequestMethod.GET)
    public String getCreateStudentPage(Model model) {
        model.addAttribute("content", "create-student");
        return "index";
    }

    @RequestMapping(value = "/students/{id}/edit", method = RequestMethod.GET)
    public String getStudentEditPage(@PathVariable("id") Long studentId, Model model) {
        var student = studentService.findOne(studentId);
        model.addAttribute("student", student);
        model.addAttribute("content", "edit-student");
        return "index";
    }

    @RequestMapping(value = "/students/{id}/view", method = RequestMethod.GET)
    public String getStudentViewPage(@PathVariable("id") Long studentId, Model model) {
        var student = studentService.findOne(studentId);
        model.addAttribute("student", student);
        model.addAttribute("content", "view-student");
        return "index";
    }

    @RequestMapping(value = "/saveStudent", method = RequestMethod.POST)
    public String saveStudent(@ModelAttribute StudentDto student, BindingResult errors, Model model) {
        // logic to process input data

        return "redirect:/students";
    }

    @RequestMapping(value = "/students/{id}/delete", method = RequestMethod.GET)
    public String deleteStudent(@PathVariable Long id, Model model) {
        studentService.delete(id);
        return "redirect:/students";
    }
}