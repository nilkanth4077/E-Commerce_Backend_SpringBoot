package com.e_commerce.controller;

import com.e_commerce.entity.Product;
import com.e_commerce.dto.CreateProductRequest;
import com.e_commerce.exception.UserException;
import com.e_commerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Product>> findProductByCategoryAndFilters(
            @RequestParam String category,
            @RequestParam List<String> color,
            @RequestParam List<String> size,
            @RequestParam int minPrice,
            @RequestParam int maxPrice,
            @RequestParam int minDiscount,
            @RequestParam String sort,
            @RequestParam String stock,
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ){
        Page<Product> res = productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
        System.out.println("Complete list of products: ");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<Product> findProductById(@PathVariable Long productId){
        Product product = productService.getProduct(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ResponseEntity<Product>(product, HttpStatus.ACCEPTED);
    }

    @PostMapping("/add")
    public Product createProduct(@RequestBody CreateProductRequest product){
        return productService.createProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) throws UserException {
        return productService.deleteProduct(id);
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

}
