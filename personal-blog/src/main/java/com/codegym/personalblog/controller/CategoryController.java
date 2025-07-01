package com.codegym.personalblog.controller;

import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // Hiển thị danh sách danh mục
    @GetMapping
    public ModelAndView list() {
        return new ModelAndView("category/list", "categories", categoryService.findAll());
    }

    // Hiển thị form tạo mới (chỉ cho ADMIN)
    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView createForm() {
        return new ModelAndView("category/create", "category", new Category());
    }

    // Lưu danh mục mới (chỉ cho ADMIN)
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String save(@ModelAttribute Category category) {
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    // Hiển thị form chỉnh sửa (chỉ cho ADMIN)
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "category/edit";
    }

    // Cập nhật danh mục (chỉ cho ADMIN)
    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    // Xoá danh mục (chỉ cho ADMIN)
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }
}
