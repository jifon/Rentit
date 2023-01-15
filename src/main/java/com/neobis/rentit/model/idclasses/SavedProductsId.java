package com.neobis.rentit.model.idclasses;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class SavedProductsId implements Serializable {

        private Long userId;
        private Long productId;

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                SavedProductsId that = (SavedProductsId) o;
                return Objects.equals(userId, that.userId) && Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() {
                return Objects.hash(userId, productId);
        }
}
