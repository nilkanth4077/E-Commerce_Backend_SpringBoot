package com.e_commerce.controller;

import com.e_commerce.entity.Cart;
import com.e_commerce.entity.User;
import com.e_commerce.exception.UserException;
import com.e_commerce.service.CartService;
import com.e_commerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private UserService userService;

    public CartController(CartService cartService, UserService userService){
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.getProfileByToken(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

}
