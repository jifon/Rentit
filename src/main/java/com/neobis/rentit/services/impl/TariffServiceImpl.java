package com.neobis.rentit.services.impl;

import com.neobis.rentit.dto.NewTariffDto;
import com.neobis.rentit.exception.NotFoundException;
import com.neobis.rentit.model.Tariff;
import com.neobis.rentit.model.User;
import com.neobis.rentit.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl {

    private final TariffRepository tariffRepository;

    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    public Tariff createTariff(NewTariffDto dto){
        Tariff entity = new Tariff(dto.getName(), dto.getDuration(), dto.getPrice(), dto.getTariffType());
        return tariffRepository.save(entity);
    }

    public Tariff findTariffById(Long tariffId) {
        return  tariffRepository.findById(tariffId).
                orElseThrow(() -> new NotFoundException("Tariff not Found") );
    }

    public String hardDeleteById(Long id) {

        tariffRepository.deleteById(id);
        return "Tariff with id:" + id + " deleted";
    }
}
