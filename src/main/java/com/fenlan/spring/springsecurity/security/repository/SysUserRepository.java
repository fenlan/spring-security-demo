package com.fenlan.spring.springsecurity.security.repository;

import com.fenlan.spring.springsecurity.security.role.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);
}
