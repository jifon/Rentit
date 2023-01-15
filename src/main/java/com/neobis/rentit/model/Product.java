package com.neobis.rentit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private String name;

    private Double price;

    @Size(max = 20)
    private String condition;

    @Size(max = 50)
    private String description;

    @Size(max = 20)
    private Integer views;

    private Boolean advertisement;

    private Integer countOfChats;

    private Integer countOfCalls;

    private String typeOfRental;

    private Double rating;

    private Boolean isActive;

    private Long numberOfReviews;

    @OneToOne
    private GeoLocation geoLocation;

    @Column(name = "date_created" /*, nullable = false **/)
    private LocalDate dateCreated;
    @Column(name = "date_updated")
    private LocalDate dateUpdated;
    @Column(name = "date_deleted")
    private LocalDateTime dateDeleted;
    @Column(name = "date_deactivated")
    private LocalDate dateDeactivated;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private ProductAddress address;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @OneToMany( cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductImages> imagesList;

    @OneToMany
    private List<ProductHasAttribute> attributesList;


    public Product(String name, Double price, String condition,
                   String description, Integer views, Boolean advertisement,
                   User user, ProductAddress address, Category category,
                   Integer countOfChats, Integer countOfCalls, String typeOfRental, LocalDate dateDeactivated, Boolean isActive) {
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.description = description;
        this.views = views;
        this.advertisement = advertisement;
        this.user = user;
        this.address = address;
        this.category = category;
        this.dateCreated = LocalDate.now();
        this.countOfChats = countOfChats;
        this.countOfCalls = countOfCalls;
        this.typeOfRental = typeOfRental;
        this.dateDeactivated = dateDeactivated;
        this.isActive = isActive;
        this.rating = 0.0;
    }

}
