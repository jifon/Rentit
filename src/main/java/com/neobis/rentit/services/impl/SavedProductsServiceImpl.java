package com.neobis.rentit.services.impl;


import com.neobis.rentit.model.Product;
import com.neobis.rentit.model.SavedProducts;
import com.neobis.rentit.model.User;
import com.neobis.rentit.repository.SavedProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedProductsServiceImpl {

    private final UserServiceImpl userService;

    private final ProductServiceImpl productService;

    private final SavedProductsRepository savedProductsRepository;

    public String like(Long productID)  {

        User user = userService.getAuthentication();
        Product product = productService.getProductById(productID);
        savedProductsRepository.save(new SavedProducts(user.getId(),product.getId()));

        return "The product was successfully liked";
    }

    public List<SavedProducts> getAll() {
        return savedProductsRepository.findAll();
    }
}
