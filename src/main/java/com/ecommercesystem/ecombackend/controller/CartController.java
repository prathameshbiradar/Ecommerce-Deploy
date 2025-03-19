package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.Cart;
import com.ecommercesystem.ecombackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add/{prodId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable int prodId, Authentication authentication) {
        String username = authentication.getName();
        Cart cart = cartService.addProductToCart(username, prodId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{prodId}")
    public ResponseEntity<Cart> deleteProductFromCart(@PathVariable int prodId, Authentication authentication) {
        String username = authentication.getName();
        Cart cart = cartService.deleteProductFromCart(username, prodId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Cart> viewCart(Authentication authentication) {
        String username = authentication.getName();
        Cart cart = cartService.getCartByUser(username);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
