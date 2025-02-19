package com.e_commerce.service;

import com.e_commerce.entity.Cart;
import com.e_commerce.entity.CartItem;
import com.e_commerce.entity.Product;
import com.e_commerce.entity.User;
import com.e_commerce.exception.UserException;
import com.e_commerce.repository.CartItemRepo;
import com.e_commerce.repository.CartRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {

    private CartItemRepo cartItemRepo;
    private UserService userService;
    private CartRepo cartRepo;

    public CartItemService(CartItemRepo cartItemRepo, UserService userService, CartRepo cartRepo){
        this.cartItemRepo = cartItemRepo;
        this.userService = userService;
        this.cartRepo = cartRepo;
    }

    public CartItem createCartItem(CartItem cartItem){
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

        CartItem createdCartItem = cartItemRepo.save(cartItem);

        return createdCartItem;
    }

    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws UserException {
        Optional<CartItem> optionalItem = cartItemRepo.findById(id);

        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            User user = userService.findCustomUserById(item.getUserId());

            if (user.getId().equals(userId)) {
                item.setQuantity(cartItem.getQuantity());
                item.setPrice(cartItem.getPrice() * cartItem.getQuantity());
                item.setDiscountedPrice(cartItem.getDiscountedPrice() * cartItem.getQuantity());
            } else {
                throw new UserException("User ID mismatch");
            }

            return cartItemRepo.save(item);
        } else {
            throw new EntityNotFoundException("CartItem not found with id: " + id);
        }
    }

    public CartItem isCartItemExist(Cart cart, Product product, Long userId){
        CartItem cartItem = cartItemRepo.isCartItemExist(cart, Optional.ofNullable(product), userId);
        return cartItem;
    }

    public void removeCartItem(Long userId, Long cartItemId) throws UserException {
        Optional<CartItem> cartItem = cartItemRepo.findById(cartItemId);
        User user = userService.findCustomUserById(cartItem.get().getUserId());

        User reqUser = userService.findCustomUserById(userId);

        if(user.getId().equals(reqUser.getId())){
            cartItemRepo.deleteById(cartItemId);
        } else {
            throw new UserException("You can't remove another user's cart item");
        }
    }

    public CartItem findCartItemById(Long cartItemId){
        Optional<CartItem> cartItem = cartItemRepo.findById(cartItemId);

        if(cartItem.isPresent()){
            return cartItem.get();
        } else {
            throw new EntityNotFoundException("Cart item not found with id: " + cartItemId);
        }
    }

}
