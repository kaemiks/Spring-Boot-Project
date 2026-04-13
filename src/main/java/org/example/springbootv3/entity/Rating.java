package org.example.springbootv3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pracownik nie może być pusty")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull(message = "Ocena nie może być pusta")
    @DecimalMin(value = "1.0", message = "Ocena musi być minimum 1.0")
    @DecimalMax(value = "5.0", message = "Ocena może być maksymalnie 5.0")
    @Column(nullable = false)
    private Double rating;

    @Size(max = 1000, message = "Komentarz może mieć maksymalnie 1000 znaków")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Konstruktory
    public Rating() {}

    public Rating(Employee employee, Double rating, String comment) {
        this.employee = employee;
        this.rating = rating;
        this.comment = comment;
    }

    // Konstruktor dla testów - bez pracownika
    public Rating(Double rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    // Konstruktor dla testów - tylko ocena
    public Rating(Double rating) {
        this.rating = rating;
    }

    // Automatyczne ustawianie dat
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Gettery i Settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}