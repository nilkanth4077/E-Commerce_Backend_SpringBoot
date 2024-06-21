package com.e_commerce.repository;

import com.e_commerce.entity.Rating;
import com.e_commerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepo extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    Review getReviewsOfProduct(@Param("productId") Long productId);
}
