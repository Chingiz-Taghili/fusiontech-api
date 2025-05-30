package com.fusiontech.api.repositories;

import com.fusiontech.api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    Optional<Category> findByName(String name);

    List<Category> findByNameContainingIgnoreCase(String keyword);
}
