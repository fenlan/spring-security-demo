package com.fenlan.spring.springsecurity.security.controller;

import com.fenlan.spring.springsecurity.security.repository.SysRoleRepository;
import com.fenlan.spring.springsecurity.security.repository.SysUserRepository;
import com.fenlan.spring.springsecurity.security.role.SysRole;
import com.fenlan.spring.springsecurity.security.role.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    SysRoleRepository roleRep;
    @Autowired
    SysUserRepository userRep;

    @GetMapping("")
    public String index(Model model, Authentication auth) {
        model.addAttribute("username", auth.toString());
        return "index";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("register")
    public String register() {
        return "register";
    }

    @PostMapping("register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {
        SysUser newUser = new SysUser();
        SysRole role = roleRep.findByName("ROLE_USER");
        newUser.setUsername(username);
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        newUser.setRoles(Arrays.asList(role));
        userRep.save(newUser);
        model.addAttribute("username", newUser);
        return "redirect:/";
    }
}

//@RestController
//@RequestMapping("/")
//public class HomeController {
//
//    @GetMapping("")
//    public ResponseEntity<Authentication> index(Authentication auth) {
//
//        HttpStatus status = HttpStatus.OK;
//        return new ResponseEntity<>(auth, status);
//    }
//
//    @GetMapping("login")
//    public ResponseEntity<String> login() {
//        return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
//    }
//}
