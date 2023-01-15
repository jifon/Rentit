package com.neobis.rentit.controller;


import com.neobis.rentit.model.Attribute;
import com.neobis.rentit.services.impl.AttributeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/attributes")
public class AttributeController {

    @Autowired
    AttributeServiceImpl attributeService;

    @GetMapping(value = "/category/{categoryId}")
    ResponseEntity<List<Attribute>> getAllAttributesByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(
                attributeService.getAttributesByCategoryId(categoryId));
    }

    @GetMapping(value = "/")
    ResponseEntity<List<Attribute>> getAllAttributes() {
        return ResponseEntity.ok(
                attributeService.getAllAttributes());
    }

    @PostMapping(value = "/{categoryId}/{attributeName}")
    ResponseEntity<Attribute> saveAttribute(@PathVariable("categoryId") Long categoryId,
                                            @PathVariable("attributeName") String attributeName) {
        return ResponseEntity.ok(
                attributeService.saveAttribute(categoryId,attributeName));
    }


    @GetMapping(value = "/{id}")
    ResponseEntity<Attribute> getAttributeById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                attributeService.getAttributeById(id));
    }


}
