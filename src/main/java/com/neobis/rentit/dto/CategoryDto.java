package com.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long categoryId;
    private String name;
    private List<SubcategoryDto> subcategoryDtoList;

    public CategoryDto(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
