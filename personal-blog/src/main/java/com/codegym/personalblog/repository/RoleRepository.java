package com.codegym.personalblog.repository;

import com.codegym.personalblog.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name); // ví dụ: ROLE_USER, ROLE_ADMIN
}
