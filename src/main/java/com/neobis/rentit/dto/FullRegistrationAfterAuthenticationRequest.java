package com.neobis.rentit.dto;

import com.neobis.rentit.model.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FullRegistrationAfterAuthenticationRequest {

    private Integer passNum;

    private String INN;

    private LocalDate issuedDatePass;

    private LocalDate expDatePass;

    private String issuedAuthorityPass;

    private String country;

    private String city;

    private String street;

}
