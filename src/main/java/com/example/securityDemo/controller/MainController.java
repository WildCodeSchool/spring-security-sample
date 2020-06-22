package com.example.securityDemo.controller;

import com.example.securityDemo.entity.User;
import com.example.securityDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/init")
    @ResponseBody
    public User init() {

        User user = new User("bastien", passwordEncoder.encode("tacos"));
        return userRepository.save(user);
    }

    // http://websystique.com/spring-security/spring-security-4-logout-example/
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    // TODO : Role entity
    // https://www.javaguides.net/2019/09/user-account-registration-and-login.html

    // TODO : register

    @GetMapping("/login")
    public String getLogIn() {

        return "login";
    }

    @GetMapping({"/", "/user"})
    @ResponseBody
    public String getUser() {

        return "Welcome user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String getAdmin() {

        return "Welcome admin";
    }
}
