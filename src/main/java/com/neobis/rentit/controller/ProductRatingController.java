package com.neobis.rentit.controller;

import com.neobis.rentit.dto.ProductRatingPost;
import com.neobis.rentit.model.Product;
import com.neobis.rentit.model.ProductRating;
import com.neobis.rentit.services.impl.ProductRatingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/productrating")
public class ProductRatingController {


    private final ProductRatingServiceImpl productRatingService;

    @PostMapping
    ResponseEntity<Product> rateProductById(@RequestBody ProductRatingPost productRatingPost) {
        return ResponseEntity.ok(productRatingService.rate(productRatingPost));
    }

    @GetMapping(value = "/{productId}")
    ResponseEntity<List<ProductRating>> getProductRatingsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productRatingService.getRatingsByProductId(productId));
    }



}
