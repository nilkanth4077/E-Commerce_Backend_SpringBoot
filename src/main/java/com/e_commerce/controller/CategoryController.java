package com.e_commerce.controller;

import com.e_commerce.entity.Category;
import com.e_commerce.entity.Product;
import com.e_commerce.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public Category addCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(Long id){
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/all")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

}
