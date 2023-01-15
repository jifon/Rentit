package com.neobis.rentit.repository;

import com.neobis.rentit.model.Product;
import com.neobis.rentit.model.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {

    boolean existsByProductId(Long id);

    @Query("SELECT r FROM ProductRating r WHERE r.product.id = ?1")
    List<ProductRating> findAllByProductId(Long id);

    @Query("SELECT AVG(pr.countOfStars) FROM ProductRating pr WHERE pr.product.id = ?1")
    Double findAverageRatingByProductId(Long id);


    Long countByProduct(Product product);
}
