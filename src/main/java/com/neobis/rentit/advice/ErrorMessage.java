package com.neobis.rentit.advice;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {

    private int statusCode;

    private LocalDateTime timestamp;

    private String message;

    private List<String> errors;
}
