package com.ecommercesystem.ecombackend.service;

import com.ecommercesystem.ecombackend.model.Product;
import com.ecommercesystem.ecombackend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp()
    {
        product = new Product(1L,"phone","redmi",19999.00,2);
    }

   @Test
   public void testGetAllProducts()
   {
       List<Product>products = Arrays.asList(product);
       when(productRepository.findAll()).thenReturn(products);
       List<Product>result=productService.getAllProducts();

       assertEquals(1,result.size());
       assertEquals("phone",result.get(0).getName());
   }
   @Test
   public void testGetAllProducts_EmptyList()
   {
       when(productRepository.findAll()).thenReturn(Collections.emptyList());

       List<Product> result = productService.getAllProducts();

       assertTrue(result.isEmpty());
   }

   @Test
    public void testGetProductById()
   {
       when(productRepository.findById(1)).thenReturn(Optional.of(product));

       Product result = productService.getProductById(1);
       assertNotNull(result);
       assertEquals("phone",result.getName());
   }

   @Test
   public void testGetProductById_NegativeId()
   {
       when(productRepository.findById(-1)).thenReturn(Optional.empty());

       Exception exception=assertThrows(RuntimeException.class,() -> productService.getProductById(-1));
       assertEquals("Product not found with an id: -1",exception.getMessage());

   }

   @Test
    public void testGetProductById_NotFound()
   {
       when(productRepository.findById(2)).thenReturn(Optional.empty());

       Exception exception = assertThrows(RuntimeException.class,()-> productService.getProductById(2));
       assertEquals("Product not found with an id: 2",exception.getMessage());
   }

   @Test
    public void testAddProduct()
   {
       when(productRepository.save(product)).thenReturn(product);

       Product result = productService.addProduct(product);

       assertNotNull(result);
       assertEquals("phone",result.getName());
   }
   @Test
   public void testAddProduct_NullProduct()
   {
       when(productRepository.save(null)).thenThrow(IllegalArgumentException.class);

       assertThrows(IllegalArgumentException.class,()->productService.addProduct(null));
   }
    @Test
    public void testAddProduct_InvalidData() {
        Product invalidProduct = new Product(2L, "", "", -5000, -1);
        when(productRepository.save(invalidProduct)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(invalidProduct));
    }

   @Test
    public void testUpdateProduct()
   {
       when(productRepository.save(product)).thenReturn(product);

       Product result= productService.updateProduct(product);

       assertNotNull(result);
       assertEquals("phone",result.getName());
   }
    @Test
    public void testUpdateProduct_NonExistingProduct() {
        when(productRepository.findById(5)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> productService.updateProduct(new Product(5L, "tablet", "samsung", 30000, 1)));

        assertEquals("Product not found with an id: 5", exception.getMessage());
    }
    @Test
    public void testUpdateProduct_InvalidData() {
        Product invalidProduct = new Product(1L, "", "", -5000, -1);

        when(productRepository.save(invalidProduct)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(invalidProduct));
    }

   @Test
    public void testDeleteProduct()
   {
       doNothing().when(productRepository).deleteById(1);
       productService.deleteProduct(1);
   }
    @Test
    public void testDeleteProduct_NonExistingProduct() {
        doThrow(new RuntimeException("Product not found with an id: 10")).when(productRepository).deleteById(10);

        Exception exception = assertThrows(RuntimeException.class, () -> productService.deleteProduct(10));
        assertEquals("Product not found with an id: 10", exception.getMessage());
    }

}
