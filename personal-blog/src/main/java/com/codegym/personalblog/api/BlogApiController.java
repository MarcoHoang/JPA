package com.codegym.personalblog.api;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.service.BlogService;
import com.codegym.personalblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs") // Base path cho tất cả các API liên quan đến Blog
public class BlogApiController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Blog>> findAllBlogs() {
        List<Blog> blogs = (List<Blog>) blogService.findAll();

        if (blogs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Blog>> findBlogsByCategory(@PathVariable Long id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Blog> blogs = categoryOptional.get().getBlogs();

        if (blogs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> findBlogById(@PathVariable Long id) {
        Optional<Blog> blogOptional = blogService.findById(id);

        if (!blogOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(blogOptional.get(), HttpStatus.OK);
    }
}