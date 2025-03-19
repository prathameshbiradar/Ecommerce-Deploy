package com.ecommercesystem.ecombackend.repository;

import com.ecommercesystem.ecombackend.model.Cart;
import com.ecommercesystem.ecombackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRespository extends JpaRepository<Cart,Long> {
    Optional<Cart>findByUser(User user);

}
