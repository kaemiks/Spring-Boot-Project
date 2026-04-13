package org.example.springbootv3.controller;

import org.example.springbootv3.entity.Rating;
import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.dto.RatingRequestDTO;
import org.example.springbootv3.dto.RatingResponseDTO;
import org.example.springbootv3.service.RatingService;
import org.example.springbootv3.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<RatingResponseDTO>> getAllRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        List<RatingResponseDTO> response = ratings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponseDTO> getRatingById(@PathVariable Long id) {
        Rating rating = ratingService.getRatingById(id);
        RatingResponseDTO response = convertToResponseDTO(rating);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<RatingResponseDTO> createRating(@RequestBody RatingRequestDTO ratingRequest) {
        System.out.println("Otrzymany JSON oceny: " + ratingRequest);

        // Znajdź pracownika
        Employee employee = null;
        if (ratingRequest.getEmployee() != null && ratingRequest.getEmployee().getId() != null) {
            employee = employeeService.getEmployeeById(ratingRequest.getEmployee().getId());
            System.out.println("Znaleziony pracownik: " + employee.getFirstName() + " " + employee.getLastName());
        }

        // Utwórz ocenę
        Rating rating = new Rating(
                employee,
                ratingRequest.getRating(),
                ratingRequest.getComment()
        );

        Rating savedRating = ratingService.saveRating(rating);
        RatingResponseDTO response = convertToResponseDTO(savedRating);

        System.out.println("Zapisana ocena: " + response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RatingResponseDTO> updateRating(@PathVariable Long id, @RequestBody RatingRequestDTO ratingRequest) {
        // Znajdź pracownika
        Employee employee = null;
        if (ratingRequest.getEmployee() != null && ratingRequest.getEmployee().getId() != null) {
            employee = employeeService.getEmployeeById(ratingRequest.getEmployee().getId());
        }

        Rating rating = new Rating(
                employee,
                ratingRequest.getRating(),
                ratingRequest.getComment()
        );

        Rating updatedRating = ratingService.updateRating(id, rating);
        RatingResponseDTO response = convertToResponseDTO(updatedRating);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<RatingResponseDTO>> getRatingsByEmployee(@PathVariable Long employeeId) {
        List<Rating> ratings = ratingService.getRatingsByEmployeeId(employeeId);
        List<RatingResponseDTO> response = ratings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Prosta metoda zamiast getRatingsAbove
    @GetMapping("/top")
    public ResponseEntity<List<RatingResponseDTO>> getTopRatings(@RequestParam(defaultValue = "4.0") Double minRating) {
        List<Rating> allRatings = ratingService.getAllRatings();
        List<Rating> topRatings = allRatings.stream()
                .filter(rating -> rating.getRating() >= minRating)
                .collect(Collectors.toList());

        List<RatingResponseDTO> response = topRatings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Metoda pomocnicza do konwersji
    private RatingResponseDTO convertToResponseDTO(Rating rating) {
        String employeeName = null;
        Long employeeId = null;

        if (rating.getEmployee() != null) {
            employeeName = rating.getEmployee().getFirstName() + " " + rating.getEmployee().getLastName();
            employeeId = rating.getEmployee().getId();
        }

        return new RatingResponseDTO(
                rating.getId(),
                rating.getRating(),
                rating.getComment(),
                employeeName,
                employeeId
        );
    }
}