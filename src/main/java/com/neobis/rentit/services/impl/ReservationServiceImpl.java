package com.neobis.rentit.services.impl;

import com.neobis.rentit.dto.ProductBasicInfo;
import com.neobis.rentit.dto.ReservationRequest;
import com.neobis.rentit.exception.BadRequestException;
import com.neobis.rentit.model.Product;
import com.neobis.rentit.model.Reservation;
import com.neobis.rentit.model.User;
import com.neobis.rentit.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl {

    private final ReservationRepository reservationRepository;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;

    public String renting(ReservationRequest request) {

        List<Reservation> productReservation = reservationRepository.findAllByProductId(request.getProductId());

        boolean isDateNotOverlapping = true;

        for(Reservation res : productReservation){
            if(isDateOverlapping(request.getStartDate(), request.getEndDate(), res.getStartDate(), res.getEndDate())){
                isDateNotOverlapping = false;
            }
        }

        if (isDateNotOverlapping) {
            Product product = productService.getProductById(request.getProductId());
            User user = userService.getCurrentUser();
            reservationRepository.save(new Reservation(request.getStartDate(),
                    request.getEndDate(), false, product, user));
            return "Successfully booked your rental time!";
        } else throw new BadRequestException("Failed to book. This time is already occupied by someone else.");
    }

    public boolean isDateOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start2.isBefore(end1) && end2.isAfter(start1);
    }

    public Reservation reject(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        reservation.setIsCancelled(true);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservationByProductId(Long productId) {
        return reservationRepository.findAllByProductId(productId);
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }
}
