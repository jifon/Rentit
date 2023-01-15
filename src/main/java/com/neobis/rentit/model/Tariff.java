package com.neobis.rentit.model;

import com.neobis.rentit.model.enums.Duration;
import com.neobis.rentit.model.enums.TariffType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Duration duration;

    private Double price;

    @Enumerated(value = EnumType.STRING)
    private TariffType tariffType;

    public Tariff(String name, Duration duration, Double price, TariffType tariffType) {
        this.name = name;
        this.duration = duration;
        this.price = price;
        this.tariffType = tariffType;
    }
}
