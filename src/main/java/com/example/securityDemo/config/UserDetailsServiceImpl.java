package com.example.securityDemo.config;

import com.example.securityDemo.entity.Role;
import com.example.securityDemo.entity.User;
import com.example.securityDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = optionalUser.get();

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // set role from Hibernate
        // https://stackoverflow.com/a/11746720
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                grantedAuthorities);
    }

    // TODO : auto login
    // https://www.baeldung.com/spring-security-auto-login-user-after-registration#authManager

    // TODO : get logged username
    // https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
}
