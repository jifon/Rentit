package com.neobis.rentit.repository;

import com.neobis.rentit.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
        , JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE p.dateDeleted is null and p.dateDeactivated >= current_date and p.user.id = :id")
    List<Product> findAllActiveProductsByUser(Long id);

    @Query("SELECT p FROM Product p WHERE p.dateDeleted is null and p.user.id = :id")
    List<Product> findAllProductsByUserId(Long id);

    @Query("SELECT p FROM Product p WHERE p.dateDeleted is null and p.dateDeactivated is null and p.category.id in :catids")
    Page<Product> findByCategoryContaining(Set<Long> catids, Pageable paging);

    @Query("SELECT p FROM Product p WHERE p.dateDeleted is null and p.dateDeactivated >= current_date  and p.id in ?1")
    List<Product> findByListOfProductIds(List<Long> productIds);

    @Query("SELECT p FROM Product p WHERE p.dateDeleted is null and p.dateDeactivated >= current_date and p.user.id in ?1")
    List<Product> findByListOfUserIds(List<Long> userIds);

    @Query(value = "SELECT * FROM products p  WHERE p.date_deleted is null and p.user_id = ?1 ORDER BY date_created DESC LIMIT 4", nativeQuery=true)
    List<Product> findFourthByOrderByCreationDate(Long userId);
}
