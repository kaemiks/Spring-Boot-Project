package org.example.springbootv3.controller;

//package com.example.employeemanagement.controller;

import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.entity.Group;
import org.example.springbootv3.service.EmployeeService;
import org.example.springbootv3.service.GroupService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Group testGroup;

    @BeforeEach
    void setUp() {
        testGroup = new Group("Zespół IT", "Zespół programistów", 10);
        testGroup.setId(1L);
    }

    @Test
    void getAllGroups_ShouldReturnGroupList() throws Exception {
        // Given
        List<Group> groups = Arrays.asList(
                testGroup,
                new Group("Zespół HR", "Zespół kadr", 5)
        );

        when(groupService.getAllGroups()).thenReturn(groups);

        // When & Then
        mockMvc.perform(get("/api/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Zespół IT"))
                .andExpect(jsonPath("$[1].name").value("Zespół HR"));

        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    void createGroup_ShouldReturnCreatedGroup() throws Exception {
        // Given
        Group newGroup = new Group("Zespół Marketing", "Zespół marketingu", 8);
        Group savedGroup = new Group("Zespół Marketing", "Zespół marketingu", 8);
        savedGroup.setId(2L);

        when(groupService.createGroup(any(Group.class))).thenReturn(savedGroup);

        // When & Then
        mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGroup)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Zespół Marketing"))
                .andExpect(jsonPath("$.description").value("Zespół marketingu"))
                .andExpect(jsonPath("$.maxMembers").value(8));

        verify(groupService, times(1)).createGroup(any(Group.class));
    }

    @Test
    void createGroup_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        Group invalidGroup = new Group("", "Opis", 0);

        // When & Then
        mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidGroup)))
                .andExpect(status().isBadRequest());

        verify(groupService, never()).createGroup(any(Group.class));
    }

    @Test
    void getGroupById_ShouldReturnGroup() throws Exception {
        // Given
        when(groupService.getGroupById(1L)).thenReturn(testGroup);

        // When & Then
        mockMvc.perform(get("/api/group/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Zespół IT"))
                .andExpect(jsonPath("$.description").value("Zespół programistów"));

        verify(groupService, times(1)).getGroupById(1L);
    }

    @Test
    void deleteGroup_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(groupService).deleteGroup(1L);

        // When & Then
        mockMvc.perform(delete("/api/group/1"))
                .andExpect(status().isNoContent());

        verify(groupService, times(1)).deleteGroup(1L);
    }

    @Test
    void getGroupEmployees_ShouldReturnEmployeeList() throws Exception {
        // Given
        List<Employee> employees = Arrays.asList(
                new Employee("Jan", "Kowalski", "jan.kowalski@example.com"),
                new Employee("Anna", "Nowak", "anna.nowak@example.com")
        );

        when(employeeService.getEmployeesByGroupId(1L)).thenReturn(employees);

        // When & Then
        mockMvc.perform(get("/api/group/1/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Jan"))
                .andExpect(jsonPath("$[1].firstName").value("Anna"));

        verify(employeeService, times(1)).getEmployeesByGroupId(1L);
    }

    @Test
    void getGroupFillPercentage_ShouldReturnFillPercentage() throws Exception {
        // Given
        when(groupService.getGroupFillPercentage(1L)).thenReturn(75.5);

        // When & Then
        mockMvc.perform(get("/api/group/1/fill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.fillPercentage").value(75.5));

        verify(groupService, times(1)).getGroupFillPercentage(1L);
    }
}