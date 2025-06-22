package com.codegym.personalblog.controller;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.service.BlogService;
import com.codegym.personalblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // Form tạo mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "blogs/create";
    }

    // Lưu blog mới
    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog) {
        if (blog.getCreatedAt() == null) {
            blog.setCreatedAt(LocalDateTime.now());
        }
        blogService.save(blog);
        return "redirect:/blogs";
    }

    // Xem chi tiết
    @GetMapping("/view/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.findById(id));
        return "blogs/view";
    }

    // Form cập nhật
    @GetMapping("/edit/{id}")
    public String editBlog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.findById(id));
        return "blogs/edit";
    }

    // Cập nhật bài viết
    @PostMapping("/update")
    public String updateBlog(@ModelAttribute Blog blog) {
        Blog existingBlog = blogService.findById(blog.getId());
        if (existingBlog != null) {
            blog.setCreatedAt(existingBlog.getCreatedAt());
        } else {
            blog.setCreatedAt(LocalDateTime.now());
        }
        blogService.save(blog);
        return "redirect:/blogs";
    }

    // Xoá bài viết
    @GetMapping("/delete/{id}")
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
