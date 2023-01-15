package com.neobis.rentit.services.impl;

import com.neobis.rentit.dto.AttributeRequest;
import com.neobis.rentit.model.ProductHasAttribute;
import com.neobis.rentit.repository.ProductHasAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductHasAttributeServiceImpl  {


    private final ProductHasAttributeRepository productHasAttributeRepository;

    private final UserServiceImpl userService;


    public List<ProductHasAttribute> saveAttributes(List<AttributeRequest> attributeRequests, Long productid){

        List<ProductHasAttribute> productHasAttributes = new ArrayList<>();

        for (AttributeRequest productAttributeDto : attributeRequests){

            ProductHasAttribute productHasAttribute = new ProductHasAttribute(
                    productid, productAttributeDto.getAttributeId(), productAttributeDto.getValue());

            productHasAttributes.add(productHasAttribute);
            productHasAttributeRepository.save(productHasAttribute);

        }
        return productHasAttributes;
    }

}
