package org.example.springbootv3.dto;

// Klasa pomocnicza
class EmployeeIdDTO {
    private Long id;

    public EmployeeIdDTO() {}

    public EmployeeIdDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
