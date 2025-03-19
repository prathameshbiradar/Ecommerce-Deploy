package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.Order;
import com.ecommercesystem.ecombackend.repository.ProductRepository;
import com.ecommercesystem.ecombackend.repository.UserRepository;
import com.ecommercesystem.ecombackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

   @Autowired
   private OrderService orderService;

   @PostMapping("/place")
   public ResponseEntity<Order> placeOrder(Authentication authentication)
   {
      String  username = authentication.getName();
      Order order = orderService.placeOrder(username);
      return new ResponseEntity<>(order, HttpStatus.OK);
   }
   @GetMapping("/{orderId}")
   public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId)
   {
      Order order =  orderService.getOrderDetails(orderId);
      return new ResponseEntity<>(order,HttpStatus.OK);
   }
   @GetMapping("/user")
   public ResponseEntity<List<Order>>getUserOrders(Authentication authentication)
   {
       String username  = authentication.getName();
       List<Order>orders = orderService.getOrderByUser(username);
       return new ResponseEntity<>(orders,HttpStatus.OK);

   }
}
