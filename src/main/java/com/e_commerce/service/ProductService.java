package com.e_commerce.service;

import com.e_commerce.entity.Category;
import com.e_commerce.entity.Product;
import com.e_commerce.repository.CategoryRepo;
import com.e_commerce.repository.ProductRepo;
import com.e_commerce.request.CreateProductRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepo productRepo;
    private CategoryRepo categoryRepo;
    private UserService userService;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo, UserService userService){
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.userService = userService;
    }

    // Create a Product
    public Product createProduct(CreateProductRequest request) {

        Category topLevel = categoryRepo.findByName(request.getTopLevelCategory());
        if(topLevel == null){
            Category topLevelCategory = new Category();
            topLevelCategory.setName(request.getTopLevelCategory());
            topLevelCategory.setLevel(1);

            topLevel = categoryRepo.save(topLevelCategory);
        }

        Category secondLevel = categoryRepo.findByNameAndParent(request.getSecondLevelCategory(), topLevel.getName());
        if(secondLevel == null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(request.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepo.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepo.findByNameAndParent(request.getThirdLevelCategory(), secondLevel.getName());
        if(thirdLevel == null){
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(request.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);

            thirdLevel = categoryRepo.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setDescription(request.getDescription());
        product.setDiscountPercent(request.getDiscountPercent());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setSizes(request.getSize());
        product.setQuantity(request.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepo.save(product);
        System.out.println("Products: " + savedProduct);

        return savedProduct;
    }

    // Delete Product By Id
    public String deleteProduct(Long id) {
        Optional<Product> product = productRepo.findById(id);
        product.get().getSizes().clear();
        productRepo.deleteById(id);
        return "Product with ID: " + id + " is deleted successfully.";
    }

    // Update Product
    public Product updateProduct(Long id, Product requestedProductForUpdate) {
        Optional<Product> optionalProduct = productRepo.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.getQuantity() != 0) {
                product.setQuantity(requestedProductForUpdate.getQuantity());
            }

            return productRepo.save(product);
        } else {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
    }

    // Find Product By Id
    public Optional<Product> getProduct(Long id) {
        Optional<Product> product = productRepo.findById(id);
        if(product.isPresent()){
            return productRepo.findById(id);
        } else {
            throw new EntityNotFoundException("Product not fount with id: " + id);
        }
    }

    // Get Product By Category
    public List<Product> getProductByCategory(String category){
        return null;
    }

    // Get All Products
    public Page<Product> getAllProduct(
            String category,
            List<String> colors,
            List<String> sizes,
            int minPrice,
            int maxPrice,
            int minDiscount,
            String sort,
            String stock,
            int pageNumber,
            int pageSize
            ){

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepo.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

        if(!colors.isEmpty()){
            products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        if(stock != null){
            if(stock.equals("in_stock")){
                products = products.stream().filter(p -> p.getQuantity() > 0)
                        .collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1)
                        .collect(Collectors.toList());
            }
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);

        Page<Product> filterProducts = new PageImpl<>(pageContent, pageable, products.size());

        return filterProducts;
    }
}
