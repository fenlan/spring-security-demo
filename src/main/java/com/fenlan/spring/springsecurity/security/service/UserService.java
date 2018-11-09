package com.fenlan.spring.springsecurity.security.service;

import com.fenlan.spring.springsecurity.security.repository.SysRoleRepository;
import com.fenlan.spring.springsecurity.security.repository.SysUserRepository;
import com.fenlan.spring.springsecurity.security.role.SysRole;
import com.fenlan.spring.springsecurity.security.role.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {
    @Autowired
    SysUserRepository userRepository;
    @Autowired
    SysRoleRepository roleRepository;

    public SysUser register(String username, String password) throws Exception {
        if (null != userRepository.findByUsername(username))
            throw new Exception("username exist!!!");
        else {
            SysUser newUser = new SysUser();
            SysRole role = roleRepository.findByName("ROLE_USER");
            newUser.setUsername(username);
            newUser.setPassword(new BCryptPasswordEncoder().encode(password));
            newUser.setRoles(Arrays.asList(role));
            userRepository.save(newUser);
            return newUser;
        }
    }

    public void changePassword(String username, String password) {
        SysUser user = userRepository.findByUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(user);
    }
}
