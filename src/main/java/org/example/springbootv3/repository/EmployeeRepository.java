package org.example.springbootv3.repository;

import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Znajdź pracownika po email
    Optional<Employee> findByEmail(String email);

    // Sprawdź czy email istnieje
    boolean existsByEmail(String email);

    // Znajdź pracowników w określonej grupie
    List<Employee> findByGroup(Group group);

    // Znajdź pracowników po ID grupy
    List<Employee> findByGroupId(Long groupId);

    // Znajdź pracowników po imieniu
    List<Employee> findByFirstNameContainingIgnoreCase(String firstName);

    // Znajdź pracowników po nazwisku
    List<Employee> findByLastNameContainingIgnoreCase(String lastName);

    // Znajdź pracowników po imieniu i nazwisku
    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByNameContaining(@Param("name") String name);
}