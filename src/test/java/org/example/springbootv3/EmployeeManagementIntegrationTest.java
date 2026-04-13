package org.example.springbootv3;

//package com.example.employeemanagement;

//package com.example.employeemanagement;

import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.entity.Group;
import org.example.springbootv3.entity.Rating;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeeManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void completeWorkflow_ShouldWorkCorrectly() throws Exception {
        // 1. Dodaj grupę
        Group group = new Group("Zespół Test", "Testowy zespół", 5);

        String groupResponse = mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Zespół Test"))
                .andReturn().getResponse().getContentAsString();

        Group savedGroup = objectMapper.readValue(groupResponse, Group.class);
        Long groupId = savedGroup.getId();

        // 2. Dodaj pracownika do grupy
        Employee employee = new Employee("Jan", "Kowalski", "jan.kowalski@test.com");
        employee.setGroup(savedGroup);

        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.email").value("jan.kowalski@test.com"));

        // 3. Sprawdź pracowników w grupie
        mockMvc.perform(get("/api/group/" + groupId + "/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Jan"));

        // 4. Sprawdź zapełnienie grupy
        mockMvc.perform(get("/api/group/" + groupId + "/fill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(groupId))
                .andExpect(jsonPath("$.fillPercentage").value(20.0)); // 1/5 * 100 = 20%

        // 5. Dodaj ocenę dla grupy
        Rating rating = new Rating(5, "Świetna praca!", savedGroup);

        mockMvc.perform(post("/api/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(5))
                .andExpect(jsonPath("$.comment").value("Świetna praca!"));

        // 6. Pobierz CSV z pracownikami
        mockMvc.perform(get("/api/employee/csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Jan,Kowalski")));

        // 7. Usuń pracownika
        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isNoContent());

        // 8. Sprawdź czy pracownik został usunięty
        mockMvc.perform(get("/api/group/" + groupId + "/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        // 9. Usuń grupę
        mockMvc.perform(delete("/api/group/" + groupId))
                .andExpect(status().isNoContent());

        // 10. Sprawdź czy grupa została usunięta
        mockMvc.perform(get("/api/group/" + groupId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployee_WithNonExistentGroup_ShouldReturnNotFound() throws Exception {
        // Given
        Group nonExistentGroup = new Group();
        nonExistentGroup.setId(999L);

        Employee employee = new Employee("Jan", "Kowalski", "jan@test.com");
        employee.setGroup(nonExistentGroup);

        // When & Then
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGroupEmployees_WithNonExistentGroup_ShouldReturnNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/group/999/employee"))
                .andExpect(status().isNotFound());
    }
}