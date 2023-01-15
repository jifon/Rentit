package com.neobis.rentit.repository;

import com.neobis.rentit.model.ProductHasAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductHasAttributeRepository extends JpaRepository<ProductHasAttribute, Long> {
}