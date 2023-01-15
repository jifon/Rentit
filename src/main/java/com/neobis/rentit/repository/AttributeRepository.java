package com.neobis.rentit.repository;

import com.neobis.rentit.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @Query("SELECT A FROM Attribute A WHERE A.category.id = ?1")
    List<Attribute> getAttributesByCategoryId(Long id);
}