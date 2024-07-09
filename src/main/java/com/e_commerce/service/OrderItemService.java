package com.e_commerce.service;

import com.e_commerce.entity.OrderItem;
import com.e_commerce.repository.OrderItemRepo;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    private OrderItemRepo orderItemRepo;

    public OrderItemService(OrderItemRepo orderItemRepo){
        this.orderItemRepo = orderItemRepo;
    }

    public OrderItem createOrderItem(OrderItem orderItem){
        return orderItemRepo.save(orderItem);
    }

}
