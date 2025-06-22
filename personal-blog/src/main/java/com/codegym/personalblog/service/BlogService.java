package com.codegym.personalblog.service;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    // Lấy tất cả blog có phân trang
    Page<Blog> findAll(Pageable pageable);

    // Tìm theo id
    Blog findById(Long id);

    // Lưu hoặc cập nhật blog
    void save(Blog blog);

    // Xoá blog
    void deleteById(Long id);

    // Tìm kiếm theo tiêu đề
    Page<Blog> findByTitleContaining(String keyword, Pageable pageable);

    // Lọc theo danh mục
    Page<Blog> findByCategory(Category category, Pageable pageable);
}
