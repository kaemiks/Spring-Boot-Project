package org.example.springbootv3.controller;

//package com.example.employeemanagement.controller;

import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee("Jan", "Kowalski", "jan.kowalski@example.com");
        testEmployee.setId(1L);
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {
        // Given
        Employee newEmployee = new Employee("Anna", "Nowak", "anna.nowak@example.com");
        Employee savedEmployee = new Employee("Anna", "Nowak", "anna.nowak@example.com");
        savedEmployee.setId(2L);

        when(employeeService nie gra.createEmployee(any(Employee.class))).thenReturn(savedEmployee);

        // When & Then
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Nowak"))
                .andExpect(jsonPath("$.email").value("anna.nowak@example.com"));

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    void createEmployee_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        Employee invalidEmployee = new Employee("", "", "invalid-email");

        // When & Then
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).createEmployee(any(Employee.class));
    }

    @Test
    void getAllEmployees_ShouldReturnEmployeeList() throws Exception {
        // Given
        List<Employee> employees = Arrays.asList(
                testEmployee,
                new Employee("Anna", "Nowak", "anna.nowak@example.com")
        );

        when(employeeService.getAllEmployees()).thenReturn(employees);

        // When & Then
        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Jan"))
                .andExpect(jsonPath("$[1].firstName").value("Anna"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee() throws Exception {
        // Given
        when(employeeService.getEmployeeById(1L)).thenReturn(testEmployee);

        // When & Then
        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void deleteEmployee_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(employeeService).deleteEmployee(1L);

        // When & Then
        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void getEmployeesAsCsv_ShouldReturnCsvFile() throws Exception {
        // Given
        String csvContent = "ID,Imię,Nazwisko,Email,Grupa\n1,Jan,Kowalski,jan.kowalski@example.com,Brak grupy\n";
        when(employeeService.generateEmployeeCsv()).thenReturn(csvContent);

        // When & Then
        mockMvc.perform(get("/api/employee/csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"employees.csv\""))
                .andExpect(content().string(csvContent));

        verify(employeeService, times(1)).generateEmployeeCsv();
    }
}