package com.neobis.rentit.repository;

import com.neobis.rentit.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT C FROM Category C WHERE C.parentCategory.id = ?1")
    List<Category> getSubCategories(Long id);

    @Query("SELECT C FROM Category C WHERE C.parentCategory IS NULL")
    List<Category> getAllCategories();

    List<Category> findByIdIn(Collection<Long> id);

}
