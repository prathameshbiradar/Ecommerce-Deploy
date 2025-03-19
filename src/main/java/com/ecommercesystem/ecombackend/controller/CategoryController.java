package com.ecommercesystem.ecombackend.controller;

import com.ecommercesystem.ecombackend.model.Category;
import com.ecommercesystem.ecombackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category)
    {
        Category savedCategory = categoryService.createCategory(category);
        return  new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Category>>getAllCategories()
    {
        List<Category> categoryList = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryList,HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Category>getCategoryById(@PathVariable Long id)
    {
       Category category= categoryService.getCategoryById(id);
       return new ResponseEntity<>(category,HttpStatus.OK);
    }
}
