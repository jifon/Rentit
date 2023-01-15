package com.neobis.rentit.dto;

import com.neobis.rentit.model.enums.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortRequest {

    @Schema(example = "dateCreated",nullable = true)
    private String fieldName;

    private SortDirection sortDirection;


}
