package com.neobis.rentit.repository;

import com.neobis.rentit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long > {

    Optional <User> findByEmail(String email);

    @Query("SELECT sum(p.rating * p.numberOfReviews) / sum(p.numberOfReviews) FROM Product p WHERE p.user.id = ?1 and p.rating > 0")
    Double findAverageRatingOfUserForProductsByUserId(Long id);

    Optional <User> findByPhoneNumber(String phoneNumber);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String code);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.dateDeleted is null and u.id = ?1")
    User findNotDeletedUserById(Long id);

    @Query("SELECT u FROM User u WHERE u.dateDeleted is null")
    List<User> findAllNotDeletedUsers();

    @Query("SELECT u FROM User u WHERE u.dateDeleted is null AND u.premiumSubscription IS TRUE " +
            "AND u.premiumEndTime > ?1")
    List<User> findAllBannerUsers(LocalDate now);

    Boolean existsByGoogleId(String googleid);

}
