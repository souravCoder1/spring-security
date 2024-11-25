package com.sourav.springsecurity.service;

import com.sourav.springsecurity.entity.Role;
import com.sourav.springsecurity.entity.User;
import com.sourav.springsecurity.repo.RoleRepo;
import com.sourav.springsecurity.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        log.info("Saving user: {}", user);
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role: {}", role);
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToExistingUser(String email, String roleName) {
        log.info("Adding role '{}' to user with email '{}'", roleName, email);
        Optional<User> user = userRepo.findByEmail(email);
        Optional<Role> role = roleRepo.findByRoleName(roleName);

        user.ifPresent(u -> role.ifPresent(r -> {
            u.getRoles().add(r);
            log.info("Role '{}' added to user '{}'", r, u);
        }));
    }
    @Override
    public User addRoleToUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUser(String email) {
        log.info("Fetching user by name: {}", email);
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAllWithRoles();
    }
    @Override
    public List<Role> getRoles() {
        log.info("Fetching all users");
        return roleRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(email + " is not present");
        }
        User user = userOptional.get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
/*
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());*/

        return new org.springframework.security.core.userdetails.User(email,
                user.getPassword(),
                grantedAuthorities
        );
    }

}
