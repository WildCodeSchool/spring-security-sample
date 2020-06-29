package com.example.securityDemo.service;

import com.example.securityDemo.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface UserService {

    void autoLogin(HttpServletRequest request, String username, String password);

    User getLoggedUsername();
}
