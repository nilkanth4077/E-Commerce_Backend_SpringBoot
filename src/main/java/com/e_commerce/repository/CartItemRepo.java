package com.e_commerce.repository;

import com.e_commerce.entity.Cart;
import com.e_commerce.entity.CartItem;
import com.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart = :cart AND ci.product = :product AND ci.userId = :userId")
    CartItem isCartItemExist(@Param("cart") Cart cart, @Param("product") Optional<Product> product, @Param("userId") Long userId);

}
