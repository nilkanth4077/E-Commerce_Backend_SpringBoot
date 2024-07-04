package com.e_commerce.service;

import com.e_commerce.entity.Address;
import com.e_commerce.entity.Order;
import com.e_commerce.entity.User;
import com.e_commerce.repository.CartRepo;
import com.e_commerce.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private CartRepo cartRepo;
    private CartService cartItemService;
    private ProductService productService;
    private OrderRepo orderRepo;

    public OrderService(CartRepo cartRepo, CartService cartItemService, ProductService productService, OrderRepo orderRepo){
        this.cartItemService = cartItemService;
        this.cartRepo = cartRepo;
        this.productService = productService;
        this.orderRepo = orderRepo;
    }

    public Order createOrder(User user, Address shippingAddress){
        return null;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}
