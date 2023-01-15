package com.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class ProductPagedRequest {

    @Schema(example = "5")
    private Long size;
    private Long page;
    @Schema(nullable = true,example = "1")
    private Long categoryId;
    private Boolean ad;
    @Schema(nullable = true)
    private String city;
    private List<SortRequest> sortRequests;
    @Schema(example = "price>30,rating:0")
    private String searchQuery;

}

