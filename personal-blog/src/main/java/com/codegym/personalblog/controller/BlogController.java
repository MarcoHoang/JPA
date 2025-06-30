package com.codegym.personalblog.controller;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.service.BlogService;
import com.codegym.personalblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping
    public String listBlogs(Model model,
                            @RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "categoryId", required = false) Long categoryId,
                            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Blog> blogs;
        if (keyword != null && !keyword.isEmpty()) {
            blogs = blogService.findByTitleContainingIgnoreCase(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else if (categoryId != null) {
            Optional<Category> categoryOptional = categoryService.findById(categoryId);
            if (categoryOptional.isPresent()) {
                blogs = blogService.findAllByCategory(categoryOptional.get(), pageable);
                model.addAttribute("selectedCategory", categoryOptional.get());
            } else {
                blogs = Page.empty(pageable);
            }
        } else {
            blogs = blogService.findAll(pageable);
        }

        model.addAttribute("blogs", blogs);
        return "blogs/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "blogs/create";
    }

    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog) {
        blogService.save(blog);
        return "redirect:/blogs";
    }

    @GetMapping("/view/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        Optional<Blog> blogOptional = blogService.findById(id);
        if (blogOptional.isPresent()) {
            model.addAttribute("blog", blogOptional.get());
            return "blogs/view";
        }
        return "error/404";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Blog> blogOptional = blogService.findById(id);
        if (blogOptional.isPresent()) {
            model.addAttribute("blog", blogOptional.get());
            return "blogs/edit";
        }
        return "error/404";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        Optional<Blog> blogOptional = blogService.findById(id);
        if (blogOptional.isPresent()) {
            model.addAttribute("blog", blogOptional.get());
            return "blogs/delete";
        }
        return "error/404";
    }

    @PostMapping("/delete")
    public String deleteBlog(@RequestParam Long id) {
        blogService.deleteById(id);
        return "redirect:/blogs";
    }

    // ✅ AJAX Search: trả về chuỗi <tr> HTML
    @GetMapping("/search")
    @ResponseBody
    public String searchBlogsAjax(@RequestParam String keyword,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Blog> blogs = blogService.findByTitleContainingIgnoreCase(keyword, pageable);
        return buildBlogRowsHtml(blogs);
    }

    // ✅ AJAX Load More: trả về chuỗi <tr> HTML
    @GetMapping("/load")
    @ResponseBody
    public String loadMoreBlogs(@RequestParam(defaultValue = "0") int offset,
                                @RequestParam(defaultValue = "5") int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("createdAt").descending());
        Page<Blog> blogs = blogService.findAll(pageable);
        return buildBlogRowsHtml(blogs);
    }

    // ✅ Hàm helper tạo chuỗi HTML các <tr>
    private String buildBlogRowsHtml(Page<Blog> blogs) {
        StringBuilder html = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Blog blog : blogs) {
            html.append("<tr>")
                    .append("<td>").append(blog.getTitle()).append("</td>")
                    .append("<td>").append(blog.getAuthor()).append("</td>")
                    .append("<td>").append(blog.getCategory() != null ? blog.getCategory().getName() : "N/A").append("</td>")
                    .append("<td>").append(blog.getCreatedAt() != null ? blog.getCreatedAt().format(formatter) : "").append("</td>")
                    .append("<td>")
                    .append("<a href='/blogs/view/").append(blog.getId()).append("'>👁 View</a> | ")
                    .append("<a href='/blogs/edit/").append(blog.getId()).append("'>✏ Edit</a> | ")
                    .append("<a href='/blogs/delete/").append(blog.getId()).append("'>🗑 Delete</a>")
                    .append("</td>")
                    .append("</tr>");
        }

        if (blogs.isEmpty()) {
            html.append("<tr><td colspan='5' style='text-align:center; font-style: italic;'>No blogs found.</td></tr>");
        }

        return html.toString();
    }
}
