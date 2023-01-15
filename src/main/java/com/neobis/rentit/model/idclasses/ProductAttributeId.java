package com.neobis.rentit.model.idclasses;


import java.io.Serializable;

public class ProductAttributeId implements Serializable {
    private Long productId;

    private Long attributeId;

    // default constructor
    public ProductAttributeId() {

    }

    public ProductAttributeId(Long productId, Long attributeId) {
        this.attributeId =attributeId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductAttributeId that = (ProductAttributeId) o;

        if (!productId.equals(that.productId)) return false;
        return attributeId.equals(that.attributeId);
    }

    @Override
    public int hashCode() {
        int result = productId.hashCode();
        result = 31 * result + attributeId.hashCode();
        return result;
    }
// equals() and hashCode()
}