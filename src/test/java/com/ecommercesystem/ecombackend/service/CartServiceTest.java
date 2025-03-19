package com.ecommercesystem.ecombackend.service;

import com.ecommercesystem.ecombackend.model.Cart;
import com.ecommercesystem.ecombackend.model.Product;
import com.ecommercesystem.ecombackend.model.User;
import com.ecommercesystem.ecombackend.repository.CartRespository;
import com.ecommercesystem.ecombackend.repository.ProductRepository;
import com.ecommercesystem.ecombackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRespository cartRespository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("Prathmesh");

        product=new Product();
        product.setId(1);
        product.setPrice(100.0);

        cart =new Cart();
        cart.setUser(user);

    }

    @Test
    public void testAddProductToCart()
    {
        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(cartRespository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRespository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.addProductToCart("Prathmesh",1);
        assertNotNull(updatedCart);
        assertEquals(1,updatedCart.getProducts().size());
    }
    @Test
    void testAddProductToCart_UserNotFound() {
        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> cartService.addProductToCart("Prathmesh", 1));
        assertEquals("User Not Found", exception.getMessage());
    }
    @Test
    void testAddProductToCart_ProductNotFound() {
        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> cartService.addProductToCart("Prathmesh", 1));
        assertEquals("Product Not Found", exception.getMessage());
    }

    @Test
    void testDeleteProductFromCart() {
        cart.getProducts().add(product);
        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(cartRespository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRespository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.deleteProductFromCart("Prathmesh", 1);

        assertNotNull(updatedCart);
        assertEquals(0, updatedCart.getProducts().size());
    }
    @Test
    void testDeleteProductFromCart_ProductNotInCart() {
        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(cartRespository.findByUser(user)).thenReturn(Optional.of(cart));

        Exception exception = assertThrows(RuntimeException.class, () -> cartService.deleteProductFromCart("Prathmesh", 1));
        assertEquals("Product Not Found in the cart", exception.getMessage());
    }
    @Test
    void testGetCartByUser() {
        when(userRepository.findByUsername("Prathmesh")).thenReturn(Optional.of(user));
        when(cartRespository.findByUser(user)).thenReturn(Optional.of(cart));

        Cart retrievedCart = cartService.getCartByUser("Prathmesh");

        assertNotNull(retrievedCart);
        assertEquals(user, retrievedCart.getUser());
    }
}
