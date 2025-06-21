package com.codegym.personalblog.controller;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogService.findAll());
        return "list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "create";
    }

    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog) {
        if (blog.getCreatedAt() == null) {
            blog.setCreatedAt(LocalDateTime.now()); // set createdAt lúc tạo mới
        }
        blogService.save(blog);
        return "redirect:/blogs";
    }

    @GetMapping("/view/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.findById(id));
        return "view";
    }

    @GetMapping("/edit/{id}")
    public String editBlog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.findById(id));
        return "edit";
    }

    @PostMapping("/update")
    public String updateBlog(@ModelAttribute Blog blog) {
        Blog existingBlog = blogService.findById(blog.getId());
        if (existingBlog != null) {
            blog.setCreatedAt(existingBlog.getCreatedAt()); // giữ nguyên createdAt cũ khi cập nhật
        } else {
            blog.setCreatedAt(LocalDateTime.now());
        }
        blogService.save(blog);
        return "redirect:/blogs";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.deleteById(id);
        return "redirect:/blogs";
    }
}
