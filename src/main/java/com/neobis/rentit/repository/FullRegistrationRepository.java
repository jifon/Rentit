package com.neobis.rentit.repository;

import com.neobis.rentit.model.FullRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FullRegistrationRepository extends JpaRepository<FullRegistration, Long> {
}
