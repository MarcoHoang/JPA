package com.codegym.personalblog.api;

import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Iterable<Category>> findAllCategories() {
        Iterable<Category> categories = categoryService.findAll();

        if (!categories.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}