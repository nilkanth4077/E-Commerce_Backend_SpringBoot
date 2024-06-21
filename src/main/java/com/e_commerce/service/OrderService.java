package com.e_commerce.service;

import com.e_commerce.entity.Address;
import com.e_commerce.entity.Order;
import com.e_commerce.entity.User;
import com.e_commerce.repository.CartRepo;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private CartRepo cartRepo;
    private CartService cartItemService;
    private ProductService productService;

    public OrderService(CartRepo cartRepo, CartService cartItemService, ProductService productService){
        this.cartItemService = cartItemService;
        this.cartRepo = cartRepo;
        this.productService = productService;
    }

    public Order createOrder(User user, Address shippingAddress){
        return null;
    }

}
