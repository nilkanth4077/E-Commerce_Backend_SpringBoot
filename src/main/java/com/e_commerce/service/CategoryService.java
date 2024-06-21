package com.e_commerce.service;

import com.e_commerce.entity.Category;
import com.e_commerce.repository.CategoryRepo;
import com.e_commerce.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private ProductRepo productRepo;
    private CategoryRepo categoryRepo;

    public CategoryService(ProductRepo productRepo, CategoryRepo categoryRepo){
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    public String deleteCategory(Long id) {
        categoryRepo.deleteById(id);
        return "Category with ID: " + id + " is deleted successfully";
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
}
