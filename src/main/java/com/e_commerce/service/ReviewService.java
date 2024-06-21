package com.e_commerce.service;

import com.e_commerce.entity.Product;
import com.e_commerce.entity.Review;
import com.e_commerce.entity.User;
import com.e_commerce.repository.ProductRepo;
import com.e_commerce.repository.ReviewRepo;
import com.e_commerce.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReviewService {

    private ReviewRepo reviewRepo;
    private ProductService productService;
    private ProductRepo productRepo;

    public ReviewService(ReviewRepo reviewRepo, ProductService productService, ProductRepo productRepo){
        this.reviewRepo = reviewRepo;
        this.productService = productService;
        this.productRepo = productRepo;
    }

    public Review createReview(ReviewRequest req, User user) {
        Product product = productService.getProduct(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepo.save(review);
    }

    public Review getAllReviewsOfProduct(Long productId) {
        return reviewRepo.getReviewsOfProduct(productId);
    }

}
