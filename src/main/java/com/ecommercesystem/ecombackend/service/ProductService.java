package com.ecommercesystem.ecombackend.service;

import com.ecommercesystem.ecombackend.model.Product;
import com.ecommercesystem.ecombackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }
    public Product getProductById(int prodId) {
      return  repo.findById(prodId).orElseThrow(() -> new RuntimeException("Product not found with an id: "+prodId));
    }

    public Product addProduct(Product product) {
        return repo.save(product);
    }

    public Product updateProduct(Product product) {

        return repo.save(product);
    }
    public void deleteProduct(int id)
    {
     repo.deleteById(id);
    }
}
