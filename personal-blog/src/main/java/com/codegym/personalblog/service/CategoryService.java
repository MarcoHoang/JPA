package com.codegym.personalblog.service;

import com.codegym.personalblog.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    void save(Category category);
    void deleteById(Long id);
}
