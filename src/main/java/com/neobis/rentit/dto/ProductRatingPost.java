package com.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRatingPost {

    private Long productId;

    private Integer rating;

    private String comment;
}
