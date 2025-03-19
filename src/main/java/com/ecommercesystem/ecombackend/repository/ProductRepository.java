package com.ecommercesystem.ecombackend.repository;

import com.ecommercesystem.ecombackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}
