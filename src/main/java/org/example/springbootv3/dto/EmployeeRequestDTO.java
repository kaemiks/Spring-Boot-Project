package org.example.springbootv3.dto;

public class EmployeeRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private GroupIdDTO group;

    // Konstruktory
    public EmployeeRequestDTO() {}

    public EmployeeRequestDTO(String firstName, String lastName, String email, GroupIdDTO group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.group = group;
    }

    // Gettery i settery
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GroupIdDTO getGroup() {
        return group;
    }

    public void setGroup(GroupIdDTO group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "EmployeeRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", groupId=" + (group != null ? group.getId() : null) +
                '}';
    }
}