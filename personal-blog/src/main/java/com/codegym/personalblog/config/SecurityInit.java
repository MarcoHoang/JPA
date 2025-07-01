package com.codegym.personalblog.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInit extends AbstractSecurityWebApplicationInitializer {
    // Không cần ghi đè gì cả – class này tự động đăng ký springSecurityFilterChain
}
