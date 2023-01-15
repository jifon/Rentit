package com.neobis.rentit.dto;

import com.neobis.rentit.model.GeoLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductSavingRequest {


    private String name;

    private Double price;

    private String condition;

    private String description;

    @Schema(example = "1")
    private Long userId;

    private String country;

    private String city;

    private String street;

    private String typeOfRental;

    @Schema(example = "1")
    private Long categoryId;

    private List<AttributeRequest> attributeRequestList;

    private GeoLocation geoLocation;


}
