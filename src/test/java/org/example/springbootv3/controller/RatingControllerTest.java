package org.example.springbootv3.controller;

//package com.example.employeemanagement.controller;

import org.example.springbootv3.entity.Group;
import org.example.springbootv3.entity.Rating;
import org.example.springbootv3.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Rating testRating;
    private Group testGroup;

    @BeforeEach
    void setUp() {
        testGroup = new Group("Zespół IT", "Zespół programistów", 10);
        testGroup.setId(1L);

        testRating = new Rating(5, "Świetna praca zespołu", testGroup);
        testRating.setId(1L);
    }

    @Test
    void createRating_ShouldReturnCreatedRating() throws Exception {
        // Given
        Rating newRating = new Rating(4, "Dobra praca", testGroup);
        Rating savedRating = new Rating(4, "Dobra praca", testGroup);
        savedRating.setId(2L);

        when(ratingService.createRating(any(Rating.class))).thenReturn(savedRating);

        // When & Then
        mockMvc.perform(post("/api/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRating)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.score").value(4))
                .andExpect(jsonPath("$.comment").value("Dobra praca"));

        verify(ratingService, times(1)).createRating(any(Rating.class));
    }

    @Test
    void createRating_WithInvalidScore_ShouldReturnBadRequest() throws Exception {
        // Given
        Rating invalidRating = new Rating(6, "Komentarz", testGroup); // Ocena poza zakresem 1-5

        // When & Then
        mockMvc.perform(post("/api/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRating)))
                .andExpect(status().isBadRequest());

        verify(ratingService, never()).createRating(any(Rating.class));
    }

    @Test
    void createRating_WithNullGroup_ShouldReturnBadRequest() throws Exception {
        // Given
        Rating invalidRating = new Rating(5, "Komentarz", null);

        // When & Then
        mockMvc.perform(post("/api/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRating)))
                .andExpect(status().isBadRequest());

        verify(ratingService, never()).createRating(any(Rating.class));
    }
}