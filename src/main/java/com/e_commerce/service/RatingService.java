package com.e_commerce.service;

import com.e_commerce.entity.Product;
import com.e_commerce.entity.Rating;
import com.e_commerce.entity.User;
import com.e_commerce.repository.RatingRepo;
import com.e_commerce.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private RatingRepo ratingRepo;
    private ProductService productService;

    public RatingService(RatingRepo ratingRepo, ProductService productService){
        this.ratingRepo = ratingRepo;
        this.productService = productService;
    }

    public Rating createRating(RatingRequest req, User user) {
        Product product = productService.getProduct(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setCreatedAt(LocalDateTime.now());
        return ratingRepo.save(rating);
    }

    public Rating getRatingsOfProduct(Long productId) {
        return ratingRepo.getRatingsOfProduct(productId);
    }

}
