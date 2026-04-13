package org.example.springbootv3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Imię nie może być puste")
    @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Nieprawidłowy format email")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "Grupa nie może być pusta")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // Konstruktory
    public Employee() {}

    public Employee(String firstName, String lastName, String email, Group group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.group = group;
    }

    // Konstruktor dla testów - bez grupy
    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Gettery i Settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }
}