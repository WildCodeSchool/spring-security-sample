package com.example.securityDemo.controller;

import com.example.securityDemo.entity.Role;
import com.example.securityDemo.entity.User;
import com.example.securityDemo.repository.RoleRepository;
import com.example.securityDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/init")
    @ResponseBody
    public User init() {

        if (!roleRepository.findByName("ROLE_USER").isPresent()) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        User user = new User("bastien", passwordEncoder.encode("tacos"));
        if (optionalRole.isPresent()) {
            user.getRoles().add(optionalRole.get());
        }
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

    // https://www.javaguides.net/2019/09/user-account-registration-and-login.html
    @GetMapping("/register")
    public String getRegister() {

        return "signup";
    }

    @PostMapping("/register")
    public String postRegister(@RequestParam String username,
                               @RequestParam String password) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        if (optionalRole.isPresent()) {
            user.getRoles().add(optionalRole.get());
            userRepository.save(user);
        }

        return "redirect:/login";
    }

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
