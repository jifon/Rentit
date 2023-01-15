package com.neobis.rentit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTransactionResponse {

    private Long transactionId;
    private Long productId;

    public ProductTransactionResponse(Long transactionId, Long productId) {
        this.transactionId = transactionId;
        this.productId = productId;
    }
}
