package com.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttributeRequest {

    @Schema(example = "8")
    private String value;

    @Schema(example = "1")
    private Long attributeId;
}
