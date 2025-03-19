package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.Cart;
import com.ecommercesystem.ecombackend.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartController cartController;

    private Cart cart;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        cart=new Cart();
    }

    @Test
    void testAddProductToCart()
    {
        when(authentication.getName()).thenReturn("Prathmesh");
        when(cartService.addProductToCart("Prathmesh",1)).thenReturn(cart);
        ResponseEntity<Cart> response = cartController.addProductToCart(1,authentication);
        assertEquals(200,response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testDeleteProductFromCart()
    {
        when(authentication.getName()).thenReturn("Prathmesh");
        when(cartService.deleteProductFromCart("Prathmesh",1)).thenReturn(cart);
        ResponseEntity<Cart> response = cartController.deleteProductFromCart(1,authentication);
        assertEquals(200,response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testViewCart()
    {
        when(authentication.getName()).thenReturn("Prathmesh");
        when(cartService.getCartByUser("Prathmesh")).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.viewCart(authentication);
        assertEquals(200,response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
