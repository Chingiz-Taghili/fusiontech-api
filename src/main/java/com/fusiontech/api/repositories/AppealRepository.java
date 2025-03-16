package com.fusiontech.api.repositories;

import com.fusiontech.api.models.Appeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppealRepository extends JpaRepository<Appeal, Long> {

    @Query("SELECT a FROM Appeal a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.surname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.message) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Appeal> findByKeywordInColumnsIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

}
