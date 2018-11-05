package com.fenlan.spring.springsecurity.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

//@Controller
//@RequestMapping("/")
//public class HomeController {
//
//    @GetMapping("")
//    public String index(Model model, Authentication auth) {
//        model.addAttribute("username", auth.getName());
//        return "index";
//    }
//
//    @GetMapping("login")
//    public String login() {
//        return "login";
//    }
//}

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public ResponseEntity<String> index(Authentication auth) {

        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(auth.getName(), status);
    }

    @GetMapping("login")
    public ResponseEntity<String> login() {
        return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
    }
}
