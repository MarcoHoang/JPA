package com.codegym.personalblog.repository;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByTitleContaining(String keyword, Pageable pageable);
    Page<Blog> findByCategory(Category category, Pageable pageable);
}
