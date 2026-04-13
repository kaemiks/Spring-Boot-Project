package org.example.springbootv3.repository;



import org.example.springbootv3.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    // Znajdź grupę po nazwie
    Optional<Group> findByName(String name);

    // Sprawdź czy nazwa grupy już istnieje
    boolean existsByName(String name);
}