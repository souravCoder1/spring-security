package com.sourav.springsecurity.service;

import com.sourav.springsecurity.entity.Role;
import com.sourav.springsecurity.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToExistingUser(String email, String roleName);
    User addRoleToUser(User user);
    Optional<User> getUser(String email);
    List<User> getUsers();
    List<Role> getRoles();
}
