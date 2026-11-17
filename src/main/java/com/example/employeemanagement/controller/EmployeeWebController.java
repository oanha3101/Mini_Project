package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.CreateEmployeeRequest;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    private final EmployeeService employeeService;

    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.findAll(null);
        model.addAttribute("employees", employees);
        return "employees/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employeeRequest", new CreateEmployeeRequest("", "", ""));
        return "employees/add";
    }

    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employeeRequest") CreateEmployeeRequest request,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (bindingResult.hasErrors()) {
            return "employees/add";
        }

        try {
            employeeService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully!");
            return "redirect:/employees/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "employees/add";
        }
    }

    @GetMapping("/search")
    public String searchEmployees(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isBlank()) {
            List<Employee> results = employeeService.search(keyword.trim());
            model.addAttribute("employees", results);
            model.addAttribute("keyword", keyword);
        }
        return "employees/search";
    }
}
