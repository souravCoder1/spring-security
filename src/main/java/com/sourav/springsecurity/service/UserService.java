package com.sourav.springsecurity.service;

import com.sourav.springsecurity.entity.Role;
import com.sourav.springsecurity.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    Optional<User> getUser(String username);
    List<User> getUsers();
}
