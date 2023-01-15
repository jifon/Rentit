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
@Table(name = "products_ratings")
public class ProductRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private Integer countOfStars;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "rater_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductRating(Integer countOfStars, String comment, User user, Product product) {
        this.countOfStars = countOfStars;
        this.comment = comment;
        this.user = user;
        this.product = product;
    }
}
