package com.codegym.personalblog.controller;

import com.codegym.personalblog.model.Role;
import com.codegym.personalblog.model.User;
import com.codegym.personalblog.service.RoleService;
import com.codegym.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;  // để gán ROLE_USER mặc định

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Trang login (Spring Security sẽ tự xử lý POST /login)
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    // Trang đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        // Kiểm tra trùng username
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại");
            return "auth/register";
        }

        // Gán role mặc định
        Role userRole = roleService.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));

        // Lưu user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userService.save(user);

        return "redirect:/login?registerSuccess";
    }

    // Trang khi bị cấm truy cập
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
