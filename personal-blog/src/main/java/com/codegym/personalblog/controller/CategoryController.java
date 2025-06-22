package com.codegym.personalblog.controller;

import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ModelAndView list() {
        return new ModelAndView("category/list", "categories", categoryService.findAll());
    }

    @GetMapping("/create")
    public ModelAndView createForm() {
        return new ModelAndView("category/create", "category", new Category());
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Category category) {
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "category/edit";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category) {
        Category existingCategory = categoryService.findById(category.getId());
        if (existingCategory != null) {
            category.setCreatedAt(existingCategory.getCreatedAt());
        } else {
            category.setCreatedAt(LocalDateTime.now());
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }
}
