package com.sourav.springsecurity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Entity
@Data
@Table(name = "app_user") // Specify a custom table name
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private String password;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL // save user with role
     //,mappedBy = "user"  //not propagate to role entity
    )
    @JoinColumn(name = "user_id") // Define the join column in the Role table
    private Set<Role> roles;
}
