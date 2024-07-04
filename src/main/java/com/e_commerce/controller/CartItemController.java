package com.e_commerce.controller;

import com.e_commerce.entity.Cart;
import com.e_commerce.entity.CartItem;
import com.e_commerce.entity.Product;
import com.e_commerce.entity.User;
import com.e_commerce.exception.UserException;
import com.e_commerce.dto.AddItemRequest;
import com.e_commerce.dto.ApiResponse;
import com.e_commerce.service.CartItemService;
import com.e_commerce.service.CartService;
import com.e_commerce.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private CartItemService cartItemService;
    private UserService userService;
    private CartService cartService;

    public CartItemController(CartItemService cartItemService, UserService userService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createCartItem(@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.getProfileByToken(jwt);
        cartService.addCartItem(user.getId(), req);

        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody CartItem cartItem) {
        try {
            CartItem updatedCartItem = cartItemService.updateCartItem(userId, id, cartItem);
            return ResponseEntity.ok(updatedCartItem);
        } catch (EntityNotFoundException | UserException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> removeCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.getProfileByToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("Item removed from cart");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> findCartItemById(@PathVariable Long id) {
        try {
            CartItem cartItem = cartItemService.findCartItemById(id);
            return ResponseEntity.ok(cartItem);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<CartItem> isCartItemExist(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam String size,
            @RequestParam Long userId) {
        CartItem cartItem = cartItemService.isCartItemExist(new Cart(cartId), new Product(productId), size, userId);
        return ResponseEntity.ok(cartItem);
    }
}

