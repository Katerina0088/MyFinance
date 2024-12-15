package com.dz.myfinance.models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @ManyToOne // Assuming a category belongs to one user
    @JoinColumn(name = "user_id") // This should match your database schema
    private User user; // Reference to the User entity

    public Category() {
    }

  }