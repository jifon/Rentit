package com.neobis.rentit.model;

import com.neobis.rentit.model.idclasses.FollowerId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(FollowerId.class)
public class Follower {

    @Id
    @Column(name = "user_id_follower")
    private Long userIdFollower;

    @Id
    @Column(name = "user_id_followed")
    private Long userIdFollowed;


}
