package com.neobis.rentit.controller;

import com.neobis.rentit.dto.FullRegistrationDto;
import com.neobis.rentit.dto.ProductBasicInfo;
import com.neobis.rentit.model.FullRegistration;
import com.neobis.rentit.services.impl.FullRegistrationServiceImpl;
import com.neobis.rentit.services.impl.UserImagesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/tech-support")
public class TechSupportController {

    private final FullRegistrationServiceImpl fullRegistrationService;
    private final UserImagesServiceImpl userImagesService;

    @PostMapping("/verify-full-registration/{applicationId}") // заявки после неполгной регистрации
    public ResponseEntity<String> verifyFullRegistrationApplication( @RequestParam Long applicationId, @RequestParam Boolean isVerify)  {
        return ResponseEntity.ok(fullRegistrationService.verifyFullRegistration(applicationId, isVerify));
    }


    @GetMapping(value = "/get-all-full-registration-applications")
    ResponseEntity<List<FullRegistrationDto>> getAllFullRegistrations() {
        return ResponseEntity.ok(
                fullRegistrationService.getAllFullRegistrations()
        );
    }





}
