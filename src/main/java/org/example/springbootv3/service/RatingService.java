package org.example.springbootv3.service;

import org.example.springbootv3.entity.Rating;
import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.repository.RatingRepository;
import org.example.springbootv3.repository.EmployeeRepository;
import org.example.springbootv3.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ocena", "id", id));
    }

    public List<Rating> getRatingsByEmployeeId(Long employeeId) {
        // Sprawdź czy pracownik istnieje
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Pracownik", "id", employeeId));

        return ratingRepository.findByEmployeeId(employeeId);
    }

    public Rating saveRating(Rating rating) {
        // Sprawdź czy pracownik istnieje
        if (rating.getEmployee() != null && rating.getEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(rating.getEmployee().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pracownik", "id", rating.getEmployee().getId()));
            rating.setEmployee(employee);
        }

        return ratingRepository.save(rating);
    }

    public Rating updateRating(Long id, Rating ratingDetails) {
        Rating rating = getRatingById(id);

        rating.setRating(ratingDetails.getRating());
        rating.setComment(ratingDetails.getComment());

        // Jeśli zmieniono pracownika
        if (ratingDetails.getEmployee() != null && ratingDetails.getEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(ratingDetails.getEmployee().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pracownik", "id", ratingDetails.getEmployee().getId()));
            rating.setEmployee(employee);
        }

        return ratingRepository.save(rating);
    }

    public void deleteRating(Long id) {
        Rating rating = getRatingById(id);
        ratingRepository.delete(rating);
    }

    public Double getAverageRatingForEmployee(Long employeeId) {
        // Sprawdź czy pracownik istnieje
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Pracownik", "id", employeeId));

        Double average = ratingRepository.findAverageRatingByEmployeeId(employeeId);
        return average != null ? average : 0.0;
    }

    public List<Rating> getTopRatings(Double minRating) {
        return ratingRepository.findTopRatings(minRating);
    }
}