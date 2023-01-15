package com.neobis.rentit.services.impl;

import com.neobis.rentit.dto.ProductRatingPost;
import com.neobis.rentit.model.Product;
import com.neobis.rentit.model.ProductRating;
import com.neobis.rentit.model.User;
import com.neobis.rentit.repository.ProductRatingRepository;
import com.neobis.rentit.repository.ProductRepository;
import com.neobis.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRatingServiceImpl {

    private final UserServiceImpl userService;

    private final ProductRatingRepository productRatingRepository;

    private final ProductServiceImpl productService;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public Product rate(ProductRatingPost productRatingPost) {

        User user = userService.getAuthentication();
        Product product = productService.getProductById(productRatingPost.getProductId());
        ProductRating productRating = new ProductRating(productRatingPost.getRating(),
                productRatingPost.getComment(), user,product);

        productRatingRepository.save(productRating);

        product.setRating(productRatingRepository.findAverageRatingByProductId(productRatingPost.getProductId()));

        product.setNumberOfReviews(productRatingRepository.countByProduct(product));
        product.getUser().setRating(userRepository.findAverageRatingOfUserForProductsByUserId(product.getUser().getId()));

        productRepository.save(product);

        return product;
    }

    public List<ProductRating> getRatingsByProductId(Long productId) {

        return productRatingRepository.findAllByProductId(productId);
    }
}
