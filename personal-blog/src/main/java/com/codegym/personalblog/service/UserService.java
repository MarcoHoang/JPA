package com.codegym.personalblog.service;

import com.codegym.personalblog.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username);
    User save(User user);
    void deleteById(Long id);
}
