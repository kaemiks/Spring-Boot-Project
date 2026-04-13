package org.example.springbootv3.dto;

public class RatingResponseDTO {
    private Long id;
    private Double rating;
    private String comment;
    private String employeeName;
    private Long employeeId;

    // Konstruktory
    public RatingResponseDTO() {}

    public RatingResponseDTO(Long id, Double rating, String comment, String employeeName, Long employeeId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.employeeName = employeeName;
        this.employeeId = employeeId;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}