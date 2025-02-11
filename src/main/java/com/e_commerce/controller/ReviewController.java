package com.e_commerce.controller;

import com.e_commerce.entity.Review;
import com.e_commerce.entity.User;
import com.e_commerce.dto.ReviewRequest;
import com.e_commerce.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public Review createReview(@RequestBody ReviewRequest req, @RequestBody User user){
        return reviewService.createReview(req, user);
    }

    @GetMapping("/all")
    public List<Review> getAllReviews(@PathVariable Long productId){
        return (List<Review>) reviewService.getAllReviewsOfProduct(productId);
    }
}
