package com.e_commerce.controller;

import com.e_commerce.entity.Rating;
import com.e_commerce.entity.User;
import com.e_commerce.dto.RatingRequest;
import com.e_commerce.service.RatingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
public class RatingController {

    private RatingService ratingService;

    public RatingController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    @PostMapping("/create")
    public Rating createRating(@RequestBody RatingRequest req, @RequestBody User user){
        return ratingService.createRating(req, user);
    }

    @GetMapping("/all")
    public Rating getAllRatings(@PathVariable Long productId){
        return ratingService.getRatingsOfProduct(productId);
    }

}
