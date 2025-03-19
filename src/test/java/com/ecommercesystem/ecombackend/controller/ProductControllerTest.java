package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.Product;
import com.ecommercesystem.ecombackend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        product = new Product(1L,"phone","redmi",19999.00,2);
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(product);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("phone"));
    }
    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(1)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("phone"));
        verify(productService, times(1)).getProductById(1);
    }
    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(2)).thenReturn(null);

        mockMvc.perform(get("/products/2"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(2);
    }

    @Test
    void testAddProduct() throws Exception {
        when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("phone"));

        verify(productService, times(1)).addProduct(any(Product.class));
    }
    @Test
    void testUpdateProduct() throws Exception {
        when(productService.getProductById(1)).thenReturn(product);
        when(productService.updateProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("phone"));

        verify(productService, times(1)).updateProduct(any(Product.class));
    }
    @Test
    void testUpdateProduct_NotFound() throws Exception {
        when(productService.getProductById(2)).thenReturn(null);

        mockMvc.perform(put("/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(2);
    }
    @Test
    void testDeleteProduct() throws Exception {
        when(productService.getProductById(1)).thenReturn(product);
        doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Successfully"));

        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        when(productService.getProductById(2)).thenReturn(null);

        mockMvc.perform(delete("/product/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product Not Found"));

        verify(productService, times(1)).getProductById(2);
    }


}
