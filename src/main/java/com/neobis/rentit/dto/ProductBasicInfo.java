package com.neobis.rentit.dto;

import com.neobis.rentit.model.Category;
import com.neobis.rentit.model.GeoLocation;
import com.neobis.rentit.model.ProductAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductBasicInfo {

    private Long id;

    private Long userid;

    private String name;

    private Double price;

    private String condition;

    private String description;

    private Integer views;

    private LocalDateTime dateCreated;

    private ProductAddress address;

    private Category category;

    private Integer countOfChats;

    private Integer countOfCalls;

    private LocalDate typeOfRental;

    private Double rating;

    private Long numberOfReviews;

    private GeoLocation geoLocation;

    private List<ProductImageDto> imagesList;

    private List<ProductAttributeDto> attributesList;

}
