package com.codegym.personalblog.repository;

import com.codegym.personalblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // Dùng để xác thực trong Spring Security
}
