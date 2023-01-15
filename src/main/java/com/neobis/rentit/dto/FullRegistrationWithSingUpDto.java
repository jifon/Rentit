package com.neobis.rentit.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class FullRegistrationWithSingUpDto {

    private String first_name;
    private String last_name;

    @NotBlank
    @Size(min = 3, max = 20)
    private String phoneNumber;

    @Size(max = 100)
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
//for full registration
    private Integer passNum;

    private String INN;

    private LocalDate issuedDatePass;

    private LocalDate expDatePass;

    private String issuedAuthorityPass;

    private String country;

    private String city;

    private String street;
}
