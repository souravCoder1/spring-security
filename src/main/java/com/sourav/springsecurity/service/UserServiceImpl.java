package com.sourav.springsecurity.service;

import com.sourav.springsecurity.entity.Role;
import com.sourav.springsecurity.entity.User;
import com.sourav.springsecurity.repo.RoleRepo;
import com.sourav.springsecurity.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public User saveUser(User user) {
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
    public Optional<User> getUser(String username) {
        log.info("Fetching user by name: {}", username);
        return userRepo.findByName(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }
}
