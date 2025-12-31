package com.paypal.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true) // No two users should have same email.
    private String email;
    private String password;

    private String role;
}
