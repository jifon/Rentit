package com.neobis.rentit.controller;


import com.neobis.rentit.dto.CategoryDto;
import com.neobis.rentit.dto.CategoryPostDto;
import com.neobis.rentit.model.Category;
import com.neobis.rentit.services.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryServiceImpl categoryService;

    @GetMapping(value = "/get-all")
    ResponseEntity<List<CategoryDto>> getAll(){
        return ResponseEntity.ok(categoryService.getAllCategoriesDto());
    }
    @GetMapping(value = "/subcategories/{id}")
    ResponseEntity<List<Category>> getAllSubCategories(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                categoryService.getSubCategories(id));
    }

    @GetMapping(value = "/subcategories/list/{id}")
    ResponseEntity<Set<Long>> getAllSubCategoriesAslist(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                categoryService.getSubCategoryTree(id));
    }

    @GetMapping(value = "/")
    ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(
                categoryService.getAllCategories());
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                categoryService.getCategoryById(id));
    }



    @PostMapping(value = "/")
    ResponseEntity<Category> saveCategory(@RequestBody CategoryPostDto categoryPostDto) {
        return ResponseEntity.ok(
                categoryService.saveCategoryByDto(categoryPostDto));
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<String> deleteCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                categoryService.deleteById(id));
    }
}
