package com.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullRegistrationDto {

    private Long id;

    private Integer passNum;

    private String INN;

    private LocalDate issuedDatePass;

    private LocalDate expDatePass;

    private String issuedAuthorityPass;

    private String country;

    private String city;

    private String street;

    private List<Long> imgIds;

}
