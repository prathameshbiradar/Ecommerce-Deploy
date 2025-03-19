package com.ecommercesystem.ecombackend.service;

import com.ecommercesystem.ecombackend.model.Cart;
import com.ecommercesystem.ecombackend.model.Order;
import com.ecommercesystem.ecombackend.model.Product;
import com.ecommercesystem.ecombackend.model.User;
import com.ecommercesystem.ecombackend.repository.CartRespository;
import com.ecommercesystem.ecombackend.repository.OrderRepository;
import com.ecommercesystem.ecombackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRespository cartRespository;
    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder() {
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product();
        product.setPrice(100.0);
        cart.setProducts(new ArrayList<>(Arrays.asList(product)));

        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.of(user));
        when(cartRespository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.placeOrder("Prathmesh");

        assertNotNull(order);
        assertEquals(1, order.getTotalQuantity());
        assertEquals(100.0, order.getTotalPrice());
    }

    @Test
    void testPlaceOrder_EmptyCart() {
        User user = new User();
        Cart cart = new Cart();
        cart.setProducts(new ArrayList<>(Arrays.asList()));

        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.of(user));
        when(cartRespository.findByUser(user)).thenReturn(Optional.of(cart));

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.placeOrder("Prathmesh"));
        assertEquals("cart is empty, order cannot placed", exception.getMessage());
    }
    @Test
    void testGetOrderDetails() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderDetails(1L);

        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    void testGetOrderDetails_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.getOrderDetails(1L));
        assertEquals("Order Not found with order Id: 1", exception.getMessage());
    }

    @Test
    void testGetOrderByUser() {
        User user = new User();
        List<Order> orders = Arrays.asList(new Order(), new Order());

        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(orders);

        List<Order> result = orderService.getOrderByUser("Prathmesh");

        assertEquals(2, result.size());
    }
}


