package com.neobis.rentit.repository;

import com.neobis.rentit.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    @Query("SELECT f.userIdFollower FROM Follower f WHERE f.userIdFollowed = ?1")
    List<Long> findAllFollowers(Long userid);

    @Query("SELECT f.userIdFollowed FROM Follower f WHERE f.userIdFollower = ?1")
    List<Long> findAllFollowing(Long userid);
}