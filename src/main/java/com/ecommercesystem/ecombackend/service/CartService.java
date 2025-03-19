package com.ecommercesystem.ecombackend.service;

import com.ecommercesystem.ecombackend.model.Cart;
import com.ecommercesystem.ecombackend.model.Product;
import com.ecommercesystem.ecombackend.model.User;
import com.ecommercesystem.ecombackend.repository.CartRespository;
import com.ecommercesystem.ecombackend.repository.ProductRepository;
import com.ecommercesystem.ecombackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRespository cartRespository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private void updateCartTotals(Cart cart) {

        int quantity = cart.getProducts().size();

        double price = 0.0;

        for(Product p: cart.getProducts())
        {
            price += p.getPrice();
        }
        cart.setTotalQuantity(quantity);
        cart.setTotalPrice(price);

    }

    private Cart getOrCreateCart(User user) {

        Optional<Cart> optionalCart = cartRespository.findByUser(user);

        if(optionalCart.isPresent())
        {
            return optionalCart.get();
        }
        else {
            Cart cart = new Cart();
            cart.setUser(user);
            return cart;
        }

    }
    public Cart addProductToCart(String username, int prodId) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
        Product product = productRepository.findById(prodId).orElseThrow(()->new RuntimeException("Product Not Found"));

        Cart cart = getOrCreateCart(user);
        cart.getProducts().add(product);
        updateCartTotals(cart);
        return cartRespository.save(cart);

    }

    public Cart deleteProductFromCart(String username, int prodId) {

        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User Not Found"));
        Product product = productRepository.findById(prodId).orElseThrow(()->new RuntimeException("Product Not found"));

        Cart cart = getOrCreateCart(user);
       boolean removed =  cart.getProducts().remove(product);

       if(!removed)
       {
           throw new RuntimeException("Product Not Found in the cart");
       }
       updateCartTotals(cart);
       return cartRespository.save(cart);

    }

    public Cart getCartByUser(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User Not Found"));

        return cartRespository.findByUser(user).orElseGet(
                () ->{
                    Cart cart = new Cart();
                    cart.setUser(user);
                    updateCartTotals(cart);
                    return cartRespository.save(cart);
                });
    }
}
