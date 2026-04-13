package org.example.springbootv3.dto;

public class RatingRequestDTO {
    private EmployeeSimpleDTO employee;
    private Double rating;
    private String comment;

    // Konstruktory
    public RatingRequestDTO() {}

    public RatingRequestDTO(EmployeeSimpleDTO employee, Double rating, String comment) {
        this.employee = employee;
        this.rating = rating;
        this.comment = comment;
    }

    // Gettery i settery
    public EmployeeSimpleDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSimpleDTO employee) {
        this.employee = employee;
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

    @Override
    public String toString() {
        return "RatingRequestDTO{" +
                "employeeId=" + (employee != null ? employee.getId() : null) +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }

    // Prosta klasa wewnętrzna
    public static class EmployeeSimpleDTO {
        private Long id;

        public EmployeeSimpleDTO() {}

        public EmployeeSimpleDTO(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}