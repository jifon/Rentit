package com.neobis.rentit.controller;

import com.neobis.rentit.dto.FollowingsProductsDto;
import com.neobis.rentit.model.Follower;
import com.neobis.rentit.payload.response.MessageResponse;
import com.neobis.rentit.services.impl.FollowerServiceImpl;
import com.neobis.rentit.services.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/follower")
public class FollowerController {

    private final FollowerServiceImpl followerService;

    @Operation(summary = "Follow user with userid by current logged in user")
    @PostMapping("/follow/{userid}")
    ResponseEntity<MessageResponse> follow(@PathVariable("userid") Long id)  {
        return ResponseEntity.ok(new MessageResponse(followerService.follow(id)));
    }

    @Operation(summary = "Get following by user ID")
    @GetMapping("/following/{userid}")
    ResponseEntity<List<Long>> getFollowingById(@PathVariable("userid") Long id)  {
        return ResponseEntity.ok((followerService.getFollowingById(id)));
    }

    @Operation(summary = "Get followers by user id")
    @GetMapping("/followers/{userid}")
    ResponseEntity<List<Long>> getFollowersById(@PathVariable("userid") Long id)  {
        return ResponseEntity.ok((followerService.getFollowersById(id)));
    }

    @Operation(summary = "Get following by user session")
    @GetMapping("/following")
    ResponseEntity<List<Long>> getFollowing()  {
        return ResponseEntity.ok((followerService.getFollowing()));
    }

    @Operation(summary = "Get followers by user session")
    @GetMapping("/followers")
    ResponseEntity<List<Long>> getFollowers()  {
        return ResponseEntity.ok((followerService.getFollowers()));
    }

    @Operation(summary = "Get all followers")
    @GetMapping("/get-all")
    ResponseEntity<List<Follower>> getAll()  {
        return ResponseEntity.ok((followerService.getAll()));
    }


    @Operation(summary = "Get all following and their products")
    @GetMapping("/following-and-products")
    ResponseEntity<List<FollowingsProductsDto>> getFollowingsProducts()  {
        return ResponseEntity.ok((followerService.getAllFollowingAndTheirProducts()));
    }


}
