package com.neobis.rentit.services.impl;

import com.neobis.rentit.dto.CategoryDto;
import com.neobis.rentit.dto.CategoryPostDto;
import com.neobis.rentit.dto.SubcategoryDto;
import com.neobis.rentit.exception.EntityStillReferencedException;
import com.neobis.rentit.exception.NotFoundException;
import com.neobis.rentit.model.Attribute;
import com.neobis.rentit.model.Category;
import com.neobis.rentit.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;

    private final AttributeServiceImpl attributeService;

    public List<CategoryDto> getAllCategoriesDto() {
        List<Category> categories = categoryRepository.getAllCategories();
        List<CategoryDto> response = new ArrayList<>();
        for(Category cat : categories){
            CategoryDto catDto = new CategoryDto(cat.getId(), cat.getName());
            List<Category> subcategory = categoryRepository.getSubCategories(cat.getId());
            List<SubcategoryDto> subcategoryDtoList = new ArrayList<>();
            for(Category sub : subcategory){
                SubcategoryDto subcatDto = new SubcategoryDto(sub.getId(), sub.getName());
                subcategoryDtoList.add(subcatDto);
            }
            catDto.setSubcategoryDtoList(subcategoryDtoList);
            response.add(catDto);
        }
        return response; 
    }


    public List<Category> getSubCategories(Long id) {
            List<Category> categories = categoryRepository.getSubCategories(id);
            return categories;
    }

    public Set<Long> getSubCategoryTree(Long id) {

        return collectLeafChildren(id,new HashSet<>());
    }

    public Set<Long> collectLeafChildren(Long id,Set<Long> results ) {
        Category category = getCategoryById(id);
        results.add(id);
        if (getSubCategories(category.getId()).isEmpty()) {
            results.add(category.getId());
        } else {
            getSubCategories(category.getId()).forEach(child -> {
                results.addAll(collectLeafChildren(child.getId(),results));
            });
        }
        return results;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->new NotFoundException(
                        String.format("Category with id %d not found", id)
                ));
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category saveCategoryByDto(CategoryPostDto categoryPostDto) {

        if (categoryPostDto.getParentCategoryId() == null){
            Category category = new Category(categoryPostDto.getName());
            return saveCategory(category);
        }
        else {
            Category category = new Category(categoryPostDto.getName(),
                    getCategoryById(categoryPostDto.getParentCategoryId()));
            return saveCategory(category);

        }



    }

    public String deleteById(Long id) {
        Category category = getCategoryById(id);
        try {
            categoryRepository.delete(category);
        }
        catch (DataIntegrityViolationException dataIntegrityViolationException){

            throw new EntityStillReferencedException(
                    String.format("Category with id %d has sub categories",id));
        }

        return String.format("Deleted category with id %d",id);
    }

    public List<Long> whichAttributesAreNotInCategory(List<Long> attributeIDs, Long categoryId){

        Category category = getCategoryById(categoryId);
        List<Long> categoryAttributeIds = attributeService.getAttributesByCategoryId(categoryId).stream().map
                (Attribute::getId).collect(Collectors.toList());

        List<Long> notInCategory = new ArrayList<>();
        for(Long attributeID : attributeIDs){
            if(!categoryAttributeIds.contains(attributeID)){
                notInCategory.add(attributeID);
            }
        }
        return notInCategory;
    }

    public List<Category> getAllCategoriesInSet(Set<Long> catids) {
        return  categoryRepository.findByIdIn(catids);
    }
}
