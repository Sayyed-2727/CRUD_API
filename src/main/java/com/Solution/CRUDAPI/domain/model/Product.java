package com.Solution.CRUDAPI.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Product entity that represents a product in the catalog.

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    // Contains only domain-related data and minimal persistence annotations.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mandatory, max 150 characters (validated at API layer).
    @Column(nullable = false, length = 150)
    private String name;

    // Optional product description.
    @Column(columnDefinition = "TEXT")
    private String description;

    // Must be greater than 0 (validated at service/API level).
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    // Must be >= 0 (validated at service/API level).    
    @Column(nullable = false)
    private Integer stock;

    // timestamp that is automatically set when entity is persisted. 
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Last update timestamp.
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Sets timestamps automatically.
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // Updates modification timestamp automatically.
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
