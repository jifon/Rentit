package com.neobis.rentit.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LikeRequest {

    @NotBlank
    private Long userId;
    @NotBlank
    private Long productId;
}
