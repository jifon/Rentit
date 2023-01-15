package com.neobis.rentit.repository;

import com.neobis.rentit.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT  res FROM Reservation res WHERE res.product.id =:productID AND res.isCancelled = false")
    List<Reservation> findAllByProductId(@RequestParam Long productID);

}
