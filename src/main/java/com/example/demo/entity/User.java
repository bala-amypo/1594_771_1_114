package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // ADMIN, STAFF

    @OneToMany(mappedBy = "serviceCounter")
    private List<ServiceCounter> serviceCounters;

    public User() {}

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    // password should be encoded in service layer
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    // default role handled in service layer
    public void setRole(String role) {
        this.role = role;
    }

    public List<ServiceCounter> getServiceCounters() {
        return serviceCounters;
    }

    public void setServiceCounters(List<ServiceCounter> serviceCounters) {
        this.serviceCounters = serviceCounters;
    }
}
