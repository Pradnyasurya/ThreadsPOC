package com.surya.threads.repository;

import com.surya.threads.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("UPDATE Product p SET p.isOfferApplied = false, p.discountPercentage = 0, p.priceAfterDiscount = p.price")
    void resetAllProductOffers();

}
