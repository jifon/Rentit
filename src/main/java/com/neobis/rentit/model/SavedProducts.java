package com.neobis.rentit.model;

import com.neobis.rentit.model.idclasses.SavedProductsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(SavedProductsId.class)
@Table(name = "saved_products")
public class SavedProducts {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "product_id")
    private Long productId;
}
