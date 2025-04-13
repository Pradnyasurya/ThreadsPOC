package com.surya.threads.service;

import com.surya.threads.entities.Product;
import com.surya.threads.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ProductServiceV2 {

    private final ProductRepository productRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ProductServiceV2(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Long> getAllProducts() {
        return productRepository.findAll().stream().map(Product::getId).toList();
    }

    @Transactional
    public String resetAllProducts() {
        productRepository.resetAllProductOffers();
        return "Reset Successfully";
    }

    public void processDiscounts(List<Long> productIds, double discountPercentage, String productCategory) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs cannot be empty");
        }
        if (discountPercentage <= 0 || discountPercentage >= 100) {
            throw new IllegalArgumentException("Discount percentage must be greater than 0 and less than 100.");
        }

        List<List<Long>> batches = splitIntoBatches(productIds, 500);

        List<CompletableFuture<Void>> futures = batches
                .stream()
                .map(
                        batch -> CompletableFuture.runAsync(() -> processProductIds(batch, discountPercentage, productCategory),executorService))
                .toList();

        //productIds.parallelStream().forEach(productId -> applyDiscount(productId, discountPercentage, productCategory));
//        List<CompletableFuture<Void>> futures = productIds.stream().map(productId ->
//                        CompletableFuture.runAsync(() -> applyDiscount(productId, discountPercentage, productCategory), executorService))
//                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    }

    private void processProductIds(List<Long> batch, double discountPercentage, String productCategory) {
        batch.forEach(item -> applyDiscount(item, discountPercentage, productCategory));
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

    private List<List<Long>> splitIntoBatches(List<Long> productIds, int batchSize) {

        int totalSize = productIds.size();
        //300 - 100 -> 3
        int batchNums = (totalSize + batchSize - 1) / batchSize;
        //calculate number of batch
        List<List<Long>> batches = new ArrayList<>();

        for (int i = 0; i < batchNums; i++) {
            int start = i * batchSize;// 0 , 51 ,100
            int end = Math.min(totalSize, (i + 1) * batchSize);// 50 , 100
            batches.add(productIds.subList(start, end));
        }


        return batches;
    }

}

