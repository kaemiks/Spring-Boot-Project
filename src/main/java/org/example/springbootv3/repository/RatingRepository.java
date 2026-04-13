package org.example.springbootv3.repository;

import org.example.springbootv3.entity.Rating;
import org.example.springbootv3.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    // Znajdź wszystkie oceny dla konkretnego pracownika
    List<Rating> findByEmployee(Employee employee);

    // Znajdź wszystkie oceny dla pracownika po ID
    List<Rating> findByEmployeeId(Long employeeId);

    // Znajdź oceny w określonym zakresie
    List<Rating> findByRatingBetween(Double minRating, Double maxRating);

    // Oblicz średnią ocenę dla pracownika
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.employee.id = :employeeId")
    Double findAverageRatingByEmployeeId(@Param("employeeId") Long employeeId);

    // Znajdź najlepsze oceny (powyżej określonej wartości)
    @Query("SELECT r FROM Rating r WHERE r.rating >= :minRating ORDER BY r.rating DESC")
    List<Rating> findTopRatings(@Param("minRating") Double minRating);
}