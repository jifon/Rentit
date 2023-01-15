package com.neobis.rentit.repository;

import com.neobis.rentit.model.ProductAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAddressRepository extends JpaRepository<ProductAddress, Long> {
}
