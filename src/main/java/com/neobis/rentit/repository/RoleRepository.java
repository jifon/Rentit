package com.neobis.rentit.repository;

import java.util.Optional;

import com.neobis.rentit.model.ERole;
import com.neobis.rentit.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}