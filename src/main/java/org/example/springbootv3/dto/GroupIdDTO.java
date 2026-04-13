package org.example.springbootv3.dto;

public class GroupIdDTO {
    private Long id;

    public GroupIdDTO() {}

    public GroupIdDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GroupIdDTO{id=" + id + "}";
    }
}