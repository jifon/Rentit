package com.neobis.rentit.repository;

import com.neobis.rentit.model.UserImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImagesRepository extends JpaRepository<UserImages, Long> {
}
