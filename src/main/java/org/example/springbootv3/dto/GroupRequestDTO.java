package org.example.springbootv3.dto;

public class GroupRequestDTO {
    private String name;
    private String description;

    // Konstruktory
    public GroupRequestDTO() {}

    public GroupRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Gettery i settery
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GroupRequestDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}