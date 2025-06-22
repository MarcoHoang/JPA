package com.codegym.personalblog.service;

import com.codegym.personalblog.model.Blog;
import com.codegym.personalblog.model.Category;
import com.codegym.personalblog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    // Phân trang
    @Override
    public Page<Blog> findAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    // Tìm theo id
    @Override
    public Blog findById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    // Lưu hoặc cập nhật
    @Override
    public void save(Blog blog) {
        if (blog.getCreatedAt() == null) {
            blog.setCreatedAt(LocalDateTime.now());
        }
        blogRepository.save(blog);
    }

    // Xoá
    @Override
    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    // Tìm kiếm theo tiêu đề
    @Override
    public Page<Blog> findByTitleContaining(String keyword, Pageable pageable) {
        return blogRepository.findByTitleContaining(keyword, pageable);
    }

    // Tìm theo danh mục
    @Override
    public Page<Blog> findByCategory(Category category, Pageable pageable) {
        return blogRepository.findByCategory(category, pageable);
    }
}
