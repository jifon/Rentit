package com.neobis.rentit.controller;

import com.neobis.rentit.dto.CategoryPostDto;
import com.neobis.rentit.dto.NewTariffDto;
import com.neobis.rentit.model.Category;
import com.neobis.rentit.model.Tariff;
import com.neobis.rentit.repository.TariffRepository;
import com.neobis.rentit.services.impl.TariffServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/tariff-settings")
public class TariffSettingsController {

    private final TariffServiceImpl tariffService;

    @Operation(summary = "Get all tariffs")
    @GetMapping("/all")
    ResponseEntity<List<Tariff>> getAllUsers() {
        return ResponseEntity.ok(tariffService.getAllTariffs());
    }

    @Operation(summary = "Get tariff by Id")
    @GetMapping("/by-id")
    ResponseEntity<Tariff> getTariffById(@PathVariable Long tariffId) {
        return ResponseEntity.ok(tariffService.findTariffById(tariffId));
    }

    @PostMapping(value = "/new")
    ResponseEntity<Tariff> saveTariff(@RequestBody NewTariffDto dto) {
        return ResponseEntity.ok(
                tariffService.createTariff(dto));
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<String> deleteTariffById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                tariffService.hardDeleteById(id));
    }

}
