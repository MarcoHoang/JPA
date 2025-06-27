package com.codegym.personalblog.service;

import com.codegym.personalblog.model.Category;
import java.util.Optional; // Import Optional

public interface CategoryService {

    Iterable<Category> findAll();

    Optional<Category> findById(Long id);

    void save(Category category);

    void deleteById(Long id);
}