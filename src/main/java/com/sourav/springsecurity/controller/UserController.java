package com.sourav.springsecurity.controller;

import com.sourav.springsecurity.entity.Role;
import com.sourav.springsecurity.entity.User;
import com.sourav.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.addRoleToUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @PostMapping("/create/role")
    public ResponseEntity<Role> createUser(@RequestBody Role user) {
        Role createdUser = userService.saveRole(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/roles/add")
    public ResponseEntity<?> addRoleToUser(@RequestParam String email, @RequestParam String roleName) {
        userService.addRoleToExistingUser(email, roleName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String email) {
        Optional<User> userOptional = userService.getUser(email);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        //List<User> users = userService.getUsers();
        //return new ResponseEntity<>(users, HttpStatus.OK);

        return ResponseEntity.ok().body(userService.getUsers());
    }
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        //List<User> users = userService.getUsers();
        //return new ResponseEntity<>(users, HttpStatus.OK);

        return ResponseEntity.ok().body(userService.getRoles());
    }
}
