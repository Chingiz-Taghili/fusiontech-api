package com.fusiontech.api.repositories;

import com.fusiontech.api.models.Slider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SliderRepository extends JpaRepository<Slider, Long> {

    @Query("SELECT s FROM Slider s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Slider> findByKeywordInColumnsIgnoreCase(@Param("keyword") String keyword);
}
