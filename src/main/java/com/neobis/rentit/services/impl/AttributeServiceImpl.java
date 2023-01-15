package com.neobis.rentit.services.impl;


import com.neobis.rentit.exception.NotFoundException;
import com.neobis.rentit.model.Attribute;
import com.neobis.rentit.repository.AttributeRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeServiceImpl {

    private final AttributeRepository attributeRepository;

    private final CategoryServiceImpl categoryService;

    public AttributeServiceImpl(AttributeRepository attributeRepository, @Lazy CategoryServiceImpl categoryService) {
        this.attributeRepository = attributeRepository;
        this.categoryService = categoryService;
    }

    public List<Attribute> getAttributesByCategoryId(Long id) {
        return attributeRepository.getAttributesByCategoryId(id);
    }

    public String getAttributeName(Long attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new NotFoundException(String.format("Attribute with id %s not found",attributeId)));
        return attribute.getName();
    }

    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    public Attribute getAttributeById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow( () -> new NotFoundException(String.format(
                        "Attribute with id %d not found",id
                )));
    }

    public Attribute saveAttribute(Attribute attribute){
            return attributeRepository.save(attribute);
    }

    public Attribute saveAttribute(Long categoryId, String attributeName) {

        return attributeRepository.save(new Attribute(attributeName,
                categoryService.getCategoryById(categoryId) ));
    }
}
