package com.sourav.springsecurity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roleId;
    private String roleName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST) // save role with user
    @JoinColumn(name = "user_id", referencedColumnName = "userId") // Define the join column in the Role table
    private User user;
}
