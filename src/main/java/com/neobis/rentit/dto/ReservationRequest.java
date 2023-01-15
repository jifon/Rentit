package com.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationRequest {

    private LocalDate startDate;
    private LocalDate endDate;
    private Long productId;
}
