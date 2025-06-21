package com.codegym.personalblog.service;

import com.codegym.personalblog.model.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> findAll();
    Blog findById(Long id);
    void save(Blog blog);
    void deleteById(Long id);
}
