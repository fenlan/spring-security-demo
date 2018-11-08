package com.fenlan.spring.springsecurity.security.repository;

import com.fenlan.spring.springsecurity.security.role.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    SysRole findByName(String name);
}
