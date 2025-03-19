package com.ecommercesystem.ecombackend.repository;

import com.ecommercesystem.ecombackend.model.Order;
import com.ecommercesystem.ecombackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUser(User user);
}
