package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.Product;
import com.ecommercesystem.ecombackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/{prodId}")
    public ResponseEntity<Product>  getProductById(@PathVariable int prodId)
    {
        Product product = service.getProductById(prodId);
        if(product != null)
        {
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product)
    {
        try {
            Product product1 = service.addProduct(product);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/products/{prodId}")
    public ResponseEntity<?> updateProduct(@PathVariable int prodId,@RequestBody Product product)
    {

        Product existingProduct = service.getProductById(prodId);

        if(existingProduct != null)
        {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            return new ResponseEntity<>(service.updateProduct(existingProduct),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id)
    {
        Product prod =service.getProductById(id);
        if(prod != null)
        {
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }
    }

}
