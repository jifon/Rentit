package com.neobis.rentit.dto;

import com.neobis.rentit.model.enums.Duration;
import com.neobis.rentit.model.enums.TariffType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class NewTariffDto {

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Duration duration;

    private Double price;

    @Enumerated(value = EnumType.STRING)
    private TariffType tariffType;

}
