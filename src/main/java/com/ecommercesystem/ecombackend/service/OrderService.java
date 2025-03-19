package com.ecommercesystem.ecombackend.service;

import com.ecommercesystem.ecombackend.model.Cart;
import com.ecommercesystem.ecombackend.model.Order;
import com.ecommercesystem.ecombackend.model.Product;
import com.ecommercesystem.ecombackend.model.User;
import com.ecommercesystem.ecombackend.repository.CartRespository;
import com.ecommercesystem.ecombackend.repository.OrderRepository;
import com.ecommercesystem.ecombackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRespository cartRespository;

    public Order placeOrder(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));

        Cart cart = cartRespository.findByUser(user).orElseThrow(()-> new RuntimeException("cart not found for user"));

        List<Product>cartProducts = cart.getProducts();
        if(cartProducts.isEmpty())
        {
            throw new RuntimeException("cart is empty, order cannot placed");
        }
        double totalPrice = 0.0;
        for(Product products : cartProducts)
        {
            totalPrice += products.getPrice();
        }
        int totalQuantity = cartProducts.size();

        Order order = new Order();
        order.setUser(user);
        order.setProduct(new ArrayList<>(cartProducts));
        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        Order savedOrder =  orderRepository.save(order);

        cart.getProducts().clear();
        cartRespository.save(cart);

        return savedOrder;

    }

    public Order getOrderDetails(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("Order Not found with order Id: "+orderId));
    }

    public List<Order> getOrderByUser(String username) {

       User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));

        return orderRepository.findByUser(user);
    }
}
