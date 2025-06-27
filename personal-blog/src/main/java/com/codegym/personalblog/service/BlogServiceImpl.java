package com.codegym.personalblog.service;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional; // Import Optional

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Page<Blog> findAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Optional<Blog> findById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public void save(Blog blog) {
        blogRepository.save(blog);
    }

    @Override
    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Iterable<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Page<Blog> findByTitleContainingIgnoreCase(String title, Pageable pageable) {
        return blogRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Page<Blog> findAllByCategory(Category category, Pageable pageable) {
        return blogRepository.findAllByCategory(category, pageable);
    }
}