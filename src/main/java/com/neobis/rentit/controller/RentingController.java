package com.neobis.rentit.controller;

import com.neobis.rentit.dto.ReservationRequest;
import com.neobis.rentit.model.Reservation;
import com.neobis.rentit.services.impl.ReservationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/renting")
public class RentingController {

    private final ReservationServiceImpl reservationService;

    @PostMapping("/rent")
    public ResponseEntity<String> saveProduct(@RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.renting(request));
    }

    @PutMapping("/reject-reservation/{reservationId}")
    public ResponseEntity<Reservation> rejectAReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.reject(reservationId));
    }

    @GetMapping(value = "/get-all-by-product/{productId}")
    ResponseEntity<List<Reservation>> getAllByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(reservationService.getAllReservationByProductId(productId));
    }

    @GetMapping("/get-all")
    ResponseEntity<List<Reservation>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    //по id product find all reservation все/актуальные по дате  (отсоврованные)
    //по id user ьронирующего find all reserv

}
