package com.codegym.personalblog.service;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional; // Import Optional

public interface BlogService {

    Iterable<Blog> findAll();

    Page<Blog> findAll(Pageable pageable);

    Optional<Blog> findById(Long id);

    void save(Blog blog);

    void deleteById(Long id);

    Page<Blog> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Blog> findAllByCategory(Category category, Pageable pageable);
}