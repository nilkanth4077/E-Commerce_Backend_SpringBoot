package com.e_commerce.repository;

import com.e_commerce.entity.Category;
import com.e_commerce.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepo extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.product.id = :productId")
    Rating getRatingsOfProduct(@Param("productId") Long productId);
}
