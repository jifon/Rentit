package com.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryPostDto {

    private String name;

    @Schema(description = "Parent Id for category. Can be null", example = "1")
    private Long parentCategoryId;
}
