package com.neobis.rentit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserBasic {

    private Long id;

    private String first_name;

    private String last_name;

    private String middle_name;

    private String email;

    private String phoneNumber;

    private Date dob;

    private LocalDateTime dateCreated;

    private LocalDateTime dateDeleted;


}
