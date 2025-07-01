package com.codegym.personalblog.service;

import com.codegym.personalblog.model.Role;

import java.util.List;

public interface RoleService {
    Role findByName(String name);
    List<Role> findAll();
    Role save(Role role);
}
