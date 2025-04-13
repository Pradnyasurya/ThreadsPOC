package com.surya.threads.controller;

import com.surya.threads.dtos.DiscountRequest;
import com.surya.threads.service.ProductService;
import com.surya.threads.service.ProductServiceV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductServiceV2 productServiceV2;

    public ProductController(ProductService productService, ProductServiceV2 productServiceV2) {
        this.productService = productService;
        this.productServiceV2 = productServiceV2;
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> getProductIds() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetAllProducts() {
        return ResponseEntity.ok(productService.resetAllProducts());
    }

    @PostMapping("/process")
    public ResponseEntity<String> processDiscounts(@RequestBody DiscountRequest discountRequest) {
        productServiceV2.processDiscounts(
                discountRequest.getProductIds(),
                discountRequest.getDiscountPercentage(),
                discountRequest.getProductCategory()
        );
        return new ResponseEntity<>("Applied Successfully",HttpStatus.OK);
    }

}
