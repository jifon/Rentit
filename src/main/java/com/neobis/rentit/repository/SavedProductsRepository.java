package com.neobis.rentit.repository;

import com.neobis.rentit.model.SavedProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SavedProductsRepository extends JpaRepository<SavedProducts, Long> {

    @Query("SELECT s.productId FROM SavedProducts s WHERE s.userId= ?1")
    List<Long> findAllSavedProductsByUserId(Long userid);

}