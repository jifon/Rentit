package com.neobis.rentit.services.impl;

import com.neobis.rentit.exception.NotFoundException;
import com.neobis.rentit.model.PasswordResetToken;
import com.neobis.rentit.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class SecurityServiceImpl {

    private Logger logger;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetToken validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);

        if (!isTokenFound(passToken)){
                throw new NotFoundException("Token not found");
            }
        else if (isTokenExpired(passToken)) {
                throw new RuntimeException("Token expired");
            }
        return passToken;

    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return passToken.getExpiryDate().isBefore(LocalDateTime.now());
    }

}
