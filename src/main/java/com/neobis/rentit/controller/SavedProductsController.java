package com.neobis.rentit.controller;

import com.neobis.rentit.model.SavedProducts;
import com.neobis.rentit.payload.response.MessageResponse;
import com.neobis.rentit.services.impl.SavedProductsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/savedproducts")
public class SavedProductsController {

    private final SavedProductsServiceImpl savedProductsService;

    @Operation(summary = "Like product with current user")
    @PostMapping("/like/{productid}")
    public ResponseEntity<MessageResponse> likeProduct(@PathVariable("productid") Long productId) {
        return ResponseEntity.ok(new MessageResponse(savedProductsService.like(productId)));
    }

    @Operation(summary = "Get all saved products")
    @GetMapping("")
    public ResponseEntity<List<SavedProducts>> getAll() {
        return ResponseEntity.ok(savedProductsService.getAll());
    }
}
