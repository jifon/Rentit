package com.neobis.rentit.dto;

import com.neobis.rentit.model.enums.Duration;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
@Setter
public class NewProductResponse {

    private String name;

    private Double price;

    private String condition;

    private String description;

    private Long userId;

    private String country;

    private String city;

    private String street;

    private String typeOfRental;

    private Long categoryId;

    private List<AttributeRequest> attributeRequestList;

    private Long tariffId;


}
