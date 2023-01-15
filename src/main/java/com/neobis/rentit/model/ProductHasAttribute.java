package com.neobis.rentit.model;

import com.neobis.rentit.model.idclasses.ProductAttributeId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product_has_attribute")
@IdClass(ProductAttributeId.class)
public class ProductHasAttribute {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Id
    private Long attributeId;

    @Size(max = 20)
    private String value;


}
