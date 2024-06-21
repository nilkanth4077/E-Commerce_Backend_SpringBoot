package com.e_commerce.service;

import com.e_commerce.entity.Cart;
import com.e_commerce.entity.CartItem;
import com.e_commerce.entity.Product;
import com.e_commerce.entity.User;
import com.e_commerce.repository.CartRepo;
import com.e_commerce.request.AddItemRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private CartRepo cartRepo;
    private CartItemService cartItemService;
    private ProductService productService;

    public CartService(CartRepo cartRepo, CartItemService cartItemService, ProductService productService){
        this.cartRepo = cartRepo;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    public Cart createCart(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepo.save(cart);
    }

    public String addCartItem(Long userId, AddItemRequest req) {
        Cart cart = cartRepo.findByUserId(userId);
        Product product = productService.getProduct(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);

            int price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }
        return "Item added to cart successfully";
    }

    public Cart findUserCart(Long userId){
        Cart cart = cartRepo.findByUserId(userId);
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for(CartItem cartItem :cart.getCartItems()){
            totalPrice = totalPrice + cartItem.getPrice();
            totalDiscountedPrice = totalDiscountedPrice + cartItem.getDiscountedPrice();
            totalItem = totalItem + cartItem.getQuantity();
        }

        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setDiscount(totalPrice - totalDiscountedPrice);

        return cartRepo.save(cart);
    }

}
