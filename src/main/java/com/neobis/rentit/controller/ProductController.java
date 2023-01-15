package com.neobis.rentit.controller;

import com.cloudinary.api.exceptions.NotFound;
import com.neobis.rentit.dto.ProductBasicInfo;
import com.neobis.rentit.dto.ProductPagedRequest;
import com.neobis.rentit.dto.ProductSavingRequest;
import com.neobis.rentit.model.Product;
import com.neobis.rentit.services.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping(value = "/get-all")
    ResponseEntity<List<?>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Product> getProductById(@PathVariable Long id ) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping(value = "/basic/{id}")
    ResponseEntity<ProductBasicInfo> getProductBasicDTOById(@PathVariable Long id ) throws NotFound {
        return ResponseEntity.ok(productService.getProductDTOById(id));

    }

    @GetMapping(value = "/basic")
    ResponseEntity<Set<ProductBasicInfo>> getProductBasicDTO( ) {
        return ResponseEntity.ok(productService.getAllProductBasicDTOs());
    }

    @PostMapping(value = "/paged")
    ResponseEntity<Page<Product>> getProductPaged(@RequestBody ProductPagedRequest productPagedRequest) {
        return ResponseEntity.ok(
                productService.getAllProductsPaged(productPagedRequest)
        );

    }

    @PostMapping(value = "/basic/paged")
    ResponseEntity<Page<ProductBasicInfo>> getProductBasicDTOPaged(@RequestBody ProductPagedRequest productPagedRequest) {
        return ResponseEntity.ok(
                productService.getAllProductsBasicPaged(productPagedRequest)
        );

    }

    @GetMapping(value = "/saved")
    ResponseEntity<List<Product>> getSavedProductsBySession() {
        return ResponseEntity.ok(
                productService.getSavedProductsBySession()
        );

    }

    @GetMapping(value = "/basic/saved")
    ResponseEntity<List<ProductBasicInfo>> getSavedBasicProductsBySession() {
        return ResponseEntity.ok(
                productService.getSavedBasicProductsBySession()
        );

    }

    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@RequestBody ProductSavingRequest productFullInfo) {
        return ResponseEntity.ok(productService.save(productFullInfo));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Product>> getProductsBySession() {
        return ResponseEntity.ok(productService.getAllActiveProductsBySession());
    }

    @PostMapping("/comparing")
    public ResponseEntity<List<ProductBasicInfo>> comparingProducts(@RequestBody List<Long> productIds) throws NotFound {
        return ResponseEntity.ok(productService.compareListOfProducts(productIds));
    }

    @GetMapping("/spec")
    @ResponseBody
    public List<Product> findAllByOrPredicate(@RequestParam String search) {

        return productService.findAllBySpec(search);
    }

    @PutMapping("/extend/{productId}")
    public Product extendDeactivationTimeById(@PathVariable Long productId, @RequestParam Long days) {

        return productService.extendDeactivationTime(productId, days);
    }

    @Operation(summary = "Hard delete product by id")
    @DeleteMapping("/hard-delete/{id}")
    ResponseEntity<?> hardDeleteProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.hardDeleteById(id));
    }



}
