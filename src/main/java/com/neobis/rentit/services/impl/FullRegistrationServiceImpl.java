package com.neobis.rentit.services.impl;

import com.neobis.rentit.dto.FullRegistrationDto;
import com.neobis.rentit.exception.NotFoundException;
import com.neobis.rentit.model.FullRegistration;
import com.neobis.rentit.model.Product;
import com.neobis.rentit.model.UserImages;
import com.neobis.rentit.model.enums.ApplicationStatus;
import com.neobis.rentit.repository.FullRegistrationRepository;
import com.neobis.rentit.services.FullRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FullRegistrationServiceImpl implements FullRegistrationService {

    private final FullRegistrationRepository fullRegistrationRepository;

    public String verifyFullRegistration(Long applicationID, Boolean isVerify){
        FullRegistration registration = fullRegistrationRepository.findById(applicationID).orElseThrow();
        if(isVerify == Boolean.TRUE){
            registration.setStatus(ApplicationStatus.ACCEPTED);
        }else{
            registration.setStatus(ApplicationStatus.DENIED);
        }
        fullRegistrationRepository.save(registration);
        return "The application was " + registration.getStatus();
    }
    public List<FullRegistrationDto> getAllFullRegistrations(){
        List<FullRegistration> all = fullRegistrationRepository.findAll();
        List<FullRegistrationDto> dtos = new ArrayList<>();
        for(FullRegistration apl : all){
            FullRegistrationDto dto = new FullRegistrationDto();
            dto.setId(apl.getId());
            dto.setPassNum(apl.getPassNum());
            dto.setINN(apl.getINN());
            dto.setIssuedDatePass(apl.getIssuedDatePass());
            dto.setExpDatePass(apl.getExpDatePass());
            dto.setIssuedAuthorityPass(apl.getIssuedAuthorityPass());
            dto.setCountry(apl.getAddress().getCountry());
            dto.setCity(apl.getAddress().getCity());
            dto.setStreet(apl.getAddress().getStreet());
            List<UserImages> images = apl.getImagesList();
            List<Long> ids = new ArrayList<>();
            for(UserImages img : images){
                ids.add(img.getId());
            }
            dto.setImgIds(ids);
            dtos.add(dto);
        }
        return dtos;
    }

    public FullRegistration getFullRegistrationById(Long id) {

        return fullRegistrationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Full registration with id %s not found", id)));
    }



}
