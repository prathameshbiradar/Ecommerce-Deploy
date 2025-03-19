package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.Order;
import com.ecommercesystem.ecombackend.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private Authentication authentication;

    @Test
    void testPlaceOrder() {
        String username = "Prathmesh";
        Order order = new Order();
        order.setId(1L);

        when(authentication.getName()).thenReturn(username);
        when(orderService.placeOrder(username)).thenReturn(order);

        ResponseEntity<Order> response = orderController.placeOrder(authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(order, response.getBody());
    }

    @Test
    void testGetOrderDetails() {
        long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderService.getOrderDetails(orderId)).thenReturn(order);

        ResponseEntity<Order> response = orderController.getOrderDetails(orderId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(order, response.getBody());
    }

    @Test
    void testGetUserOrders() {
        String username = "Prathmesh";
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);

        when(authentication.getName()).thenReturn(username);
        when(orderService.getOrderByUser(username)).thenReturn(orders);

        ResponseEntity<List<Order>> response = orderController.getUserOrders(authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orders, response.getBody());
    }
}
