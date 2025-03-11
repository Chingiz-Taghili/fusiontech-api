package com.fusiontech.api.repositories;

import com.fusiontech.api.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    List<Product> findByCategoryId(Long categoryId);

    Page<Product> findBySubcategoryId(Long subcategoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR (CAST(p.price AS string) LIKE CONCAT('%', :keyword, '%')) " +
            "OR (CAST(p.discountPrice AS string) LIKE CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.brand.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> findByKeywordInColumnsIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    Page<Product> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

    Page<Product> findByPriceBetweenAndCategoryId(double minPrice, double maxPrice, Long categoryId, Pageable pageable);

    Page<Product> findByPriceBetweenAndBrandId(double minPrice, double maxPrice, Long brandId, Pageable pageable);

    Page<Product> findByPriceBetweenAndCategoryIdAndBrandId(
            double minPrice, double maxPrice, Long categoryId, Long brandId, Pageable pageable);

    List<Product> findByFeaturedTrue();

    List<Product> findByOfferedTrue();

    long countByPriceLessThanEqual(double price);

    long countByPriceBetween(double startPrice, double endPrice);

    long countByPriceGreaterThan(double price);
}