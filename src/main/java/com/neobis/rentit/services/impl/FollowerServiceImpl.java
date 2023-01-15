package com.neobis.rentit.services.impl;

import com.neobis.rentit.dto.FollowingsProductsDto;
import com.neobis.rentit.model.Follower;
import com.neobis.rentit.model.User;
import com.neobis.rentit.repository.FollowerRepository;
import com.neobis.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowerServiceImpl {

    private final UserServiceImpl userService;
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;
    private final ProductServiceImpl productService;

    public List<FollowingsProductsDto> getAllFollowingAndTheirProducts(){
        User currentUser = userService.getAuthentication();
        List<Long> users = followerRepository.findAllFollowing(currentUser.getId());
        List<FollowingsProductsDto> response = new ArrayList<>();
        for(Long u : users){
            FollowingsProductsDto dto = new FollowingsProductsDto();
            Optional<User> user = userRepository.findById(u);
            dto.setId(user.get().getId());
            dto.setFirstName(user.get().getFirstName());
            dto.setLastName(user.get().getLastName());
            dto.setMiddleName(user.get().getMiddleName());
            dto.setEmail(user.get().getEmail());
            dto.setPhoneNumber(user.get().getPhoneNumber());
            dto.setRating(userService.getUserRating(user.get()));
            dto.setProducts(productService.getLastFourProductsByUserId(user.get()));
            response.add(dto);
        }
        return response;
    }


    public String follow(Long followedID){

        User follower = userService.getAuthentication();
        User followed = userService.isUserDeletedCheck(followedID);

        followerRepository.save(new Follower(follower.getId(), followed.getId()));

        return "The user was successfully subscribed";
    }

    public List<Long> getFollowersById(Long id) {

        return followerRepository.findAllFollowers(id);
    }

    public List<Long> getFollowingById(Long id) {

        return followerRepository.findAllFollowing(id);
    }

    public List<Long> getFollowers() {

        User user = userService.getAuthentication();
        return followerRepository.findAllFollowers(user.getId());
    }

    public List<Long> getFollowing() {
        User user = userService.getAuthentication();
        return followerRepository.findAllFollowing(user.getId());
    }

    public List<Follower> getAll() {
        return followerRepository.findAll();
    }
}
