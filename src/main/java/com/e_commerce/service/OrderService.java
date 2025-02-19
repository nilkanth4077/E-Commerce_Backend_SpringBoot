package com.e_commerce.service;

import com.e_commerce.entity.*;
import com.e_commerce.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private CartRepo cartRepo;
    private CartService cartService;
    private CartService cartItemService;
    private ProductService productService;
    private OrderRepo orderRepo;
    private AddressRepo addressRepo;
    private UserRepo userRepo;
    private OrderItemRepo orderItemRepo;

    public OrderService(CartRepo cartRepo, CartService cartService, CartService cartItemService, ProductService productService, OrderRepo orderRepo, AddressRepo addressRepo, UserRepo userRepo, OrderItemRepo orderItemRepo){
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.cartRepo = cartRepo;
        this.productService = productService;
        this.orderRepo = orderRepo;
        this.addressRepo = addressRepo;
        this.userRepo = userRepo;
        this.orderItemRepo = orderItemRepo;
    }

    public Order createOrder(User user, Address shippingAddress){

        shippingAddress.setUser(user);
        Address address = addressRepo.save(shippingAddress);

        user.getAddress().add(address);
        userRepo.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item: cart.getCartItems()){
            OrderItem orderItem = new OrderItem();

            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createdOrderItem = orderItemRepo.save(orderItem);

            orderItems.add(createdOrderItem);

        }

        Order createdOrder = new Order();

        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
//        createdOrder.getPaymentDetails().setStatus("PENDING");
        createdOrder.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepo.save(createdOrder);

        for (OrderItem item: orderItems){
            item.setOrder(savedOrder);
            orderItemRepo.save(item);
        }

        return savedOrder;
    }

    public Order placedOrder(Long orderId){
        Order order = orderRepo.findOrderById(orderId);
        order.setOrderStatus("PLACED");
//        order.getPaymentDetails().setStatus("COMPLETED");
        return order;
    }

    public Order shippedOrder(Long orderId){
        Order order = orderRepo.findOrderById(orderId);
//        order.getPaymentDetails().setStatus("SHIPPED");
        return orderRepo.save(order);
    }

    public Order deliveredOrder(Long orderId){
        Order order = orderRepo.findOrderById(orderId);
//        order.getPaymentDetails().setStatus("DELIVERED");
        return orderRepo.save(order);
    }

    public Order cancelledOrder(Long orderId){
        Order order = orderRepo.findOrderById(orderId);
//        order.getPaymentDetails().setStatus("CANCELLED");
        return orderRepo.save(order);
    }

    public Order confirmedOrder(Long orderId){
        Order order = orderRepo.findOrderById(orderId);
//        order.getPaymentDetails().setStatus("CONFIRMED");
        return orderRepo.save(order);
    }

    public Order findOrderById(Long orderId){
        Optional<Order> order = orderRepo.findById(orderId);

        if(order.isPresent()){
            return order.get();
        }
        throw new RuntimeException("Order not exist with id: " + orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public List<Order> usersOrderHistory(Long userId) {
        List<Order> orders = orderRepo.getUsersOrders(userId);
        return orders;
    }

    public void deleteOrder(Long orderId){
        Order order = orderRepo.findOrderById(orderId);
        orderRepo.deleteById(orderId);
    }
}
