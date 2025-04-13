package com.surya.threads.service;

import com.surya.threads.entities.Product;
import com.surya.threads.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Long> getAllProducts() {
        return productRepository.findAll().stream().map(Product::getId).toList();
    }

//    public String resetAllProducts() {
//        productRepository.findAll().forEach(product ->  {
//            product.setOfferApplied(false);
//            product.setDiscountPercentage(0);
//            product.setPriceAfterDiscount(product.getPrice());
//            productRepository.save(product);
//        });
//        return "Reset Successfully";
//    }

    @Transactional
    public String resetAllProducts() {
        productRepository.resetAllProductOffers();
        return "Reset Successfully";
    }

    @Transactional
    public void processDiscounts(List<Long> productIds, double discountPercentage, String productCategory) {

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs cannot be empty");
        }
        if (discountPercentage <= 0 || discountPercentage >= 100) {
            throw new IllegalArgumentException("Discount percentage must be greater than 0 and less than 100.");
        }

        productIds.parallelStream().forEach(productId -> applyDiscount(productId, discountPercentage, productCategory));

    }

    @Transactional
    protected void applyDiscount(Long productId, double discountPercentage, String productCategory) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getCategory().equalsIgnoreCase(productCategory)) {
            double discountedPrice = product.getPrice() - (product.getPrice() * (discountPercentage / 100));
            product.setPriceAfterDiscount(discountedPrice);
            product.setOfferApplied(true);
            product.setDiscountPercentage(discountPercentage);
        }
        productRepository.save(product);
    }

}
