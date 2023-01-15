package com.neobis.rentit.model;

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
@Table(name = "product_address")
public class ProductAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private String country;

    @Size(max = 20)
    private String city;

    @Size(max = 20)
    private String street;


    public ProductAddress(String country, String city, String street) {
        this.country = country;
        this.city = city;
        this.street = street;
    }
}
