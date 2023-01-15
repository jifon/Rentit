package com.neobis.rentit.services;

import com.neobis.rentit.model.Product;
import com.neobis.rentit.dto.ProductSavingRequest;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product save(ProductSavingRequest dto);
}
