package com.codegym.personalblog.controller;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.service.BlogService;
import com.codegym.personalblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    // Đặt danh sách category vào mọi view
    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryService.findAll();
    }

    // Danh sách blogs có phân trang và sắp xếp
    @GetMapping
    public String listBlogs(Model model,
                            @PageableDefault(size = 5, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
                            Pageable pageable) {
        Page<Blog> blogs = blogService.findAll(pageable);
        model.addAttribute("blogs", blogs);
        return "blogs/list";
    }

    // Form tạo mới - chỉ cho phép người đăng nhập
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String showCreateForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "blogs/create";
    }

    // Lưu blog mới - chỉ cho phép người đăng nhập
    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public String saveBlog(@ModelAttribute Blog blog, Authentication authentication) {
        if (blog.getCreatedAt() == null) {
            blog.setCreatedAt(LocalDateTime.now());
        }

        // Gán người dùng hiện tại làm tác giả
        blog.setAuthor(authentication.getName());

        blogService.save(blog);
        return "redirect:/blogs";
    }

    // Xem chi tiết bài viết
    @GetMapping("/view/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.findById(id));
        return "blogs/view";
    }

    // Form cập nhật - chỉ người đăng nhập
    @GetMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editBlog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.findById(id));
        return "blogs/edit";
    }

    // Cập nhật bài viết - chỉ người đăng nhập
    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public String updateBlog(@ModelAttribute Blog blog, Authentication authentication) {
        Blog existingBlog = blogService.findById(blog.getId());
        if (existingBlog != null) {
            blog.setCreatedAt(existingBlog.getCreatedAt());
        } else {
            blog.setCreatedAt(LocalDateTime.now());
        }

        // Cập nhật tác giả thành người hiện tại (nếu muốn giữ nguyên người cũ thì bỏ dòng dưới)
        blog.setAuthor(authentication.getName());

        blogService.save(blog);
        return "redirect:/blogs";
    }

    // Xoá bài viết - chỉ người đăng nhập
    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteBlog(@PathVariable Long id) {
        blogService.deleteById(id);
        return "redirect:/blogs";
    }

    // Tìm kiếm theo tiêu đề
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model,
                         @PageableDefault(size = 5, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
                         Pageable pageable) {
        Page<Blog> blogs = blogService.findByTitleContaining(keyword, pageable);
        model.addAttribute("blogs", blogs);
        model.addAttribute("keyword", keyword);
        return "blogs/list";
    }

    // Hiển thị theo danh mục
    @GetMapping("/category/{id}")
    public String blogsByCategory(@PathVariable Long id, Model model,
                                  @PageableDefault(size = 5, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
                                  Pageable pageable) {
        Category category = categoryService.findById(id);
        Page<Blog> blogs = blogService.findByCategory(category, pageable);
        model.addAttribute("blogs", blogs);
        model.addAttribute("selectedCategory", category.getName());
        return "blogs/list";
    }
}
