package com.fusiontech.api.repositories;

import com.fusiontech.api.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findAllByOrderByNumberAsc();
}