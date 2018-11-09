package com.fenlan.spring.springsecurity.security.controller;

import com.fenlan.spring.springsecurity.security.repository.SysRoleRepository;
import com.fenlan.spring.springsecurity.security.repository.SysUserRepository;
import com.fenlan.spring.springsecurity.security.role.SysRole;
import com.fenlan.spring.springsecurity.security.role.SysUser;
import com.fenlan.spring.springsecurity.security.service.UserService;
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
    UserService userService;

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
        try {
            model.addAttribute("username", userService.register(username, password));
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "register";
        }
    }
}

//@RestController
//@RequestMapping("/")
//public class HomeController {
//
//    @Autowired
//    UserService service;
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
//
//    @PostMapping("register")
//    public ResponseEntity<String> register(@RequestParam("username") String username,
//                                           @RequestParam("password") String password) {
//        try {
//            service.register(username, password);
//            HttpStatus status = HttpStatus.OK;
//            return new ResponseEntity<>("successful!!!", status);
//        } catch (Exception e) {
//            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//            return new ResponseEntity<>(e.getLocalizedMessage(), status);
//        }
//    }
//}
