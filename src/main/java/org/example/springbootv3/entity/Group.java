package org.example.springbootv3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa grupy nie może być pusta")
    @Size(min = 2, max = 100, message = "Nazwa grupy musi mieć od 2 do 100 znaków")
    @Column(unique = true, nullable = false)
    private String name;

    @Size(max = 500, message = "Opis może mieć maksymalnie 500 znaków")
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();

    // Konstruktory
    public Group() {
        this.employees = new ArrayList<>();
    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
        this.employees = new ArrayList<>();
    }

    // Konstruktor dla testów - tylko nazwa
    public Group(String name) {
        this.name = name;
    }

    // Gettery i Settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}