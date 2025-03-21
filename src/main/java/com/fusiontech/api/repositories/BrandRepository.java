package com.fusiontech.api.repositories;

import com.fusiontech.api.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    List<Brand> findByNameContainingIgnoreCase(String keyword);
}
