package com.neobis.rentit.services.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.neobis.rentit.exception.EmailAlreadyExistsException;
import com.neobis.rentit.exception.NotFoundException;
import com.neobis.rentit.exception.PhoneNumberAlreadyExistsException;
import com.neobis.rentit.exception.TokenRefreshException;
import com.neobis.rentit.model.*;
import com.neobis.rentit.payload.request.LoginRequest;
import com.neobis.rentit.payload.request.SignupRequest;
import com.neobis.rentit.payload.request.TokenRefreshRequest;
import com.neobis.rentit.payload.response.JwtResponse;
import com.neobis.rentit.payload.response.TokenRefreshResponse;
import com.neobis.rentit.repository.RoleRepository;
import com.neobis.rentit.repository.UserRepository;
import com.neobis.rentit.security.jwt.JwtUtils;
import com.neobis.rentit.security.services.UserDetailsImpl;
import com.neobis.rentit.utils.EmailUtility;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl {


    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenServiceImpl refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;

    private final JavaMailSender mailSender;
    private final UserServiceImpl userService;
    private final SecurityServiceImpl securityService;

    private final FirebaseAuth firebaseAuth;



    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        loginRequest.setUsername(loginRequest.getUsername().toLowerCase());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());



        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPhoneNumber(),
                roles));

    }

    public User registerUser(SignupRequest signUpRequest) throws MessagingException, UnsupportedEncodingException {
        if (signUpRequest.getPhoneNumber() != null &
                userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw  new PhoneNumberAlreadyExistsException("Error: phone-number "+ signUpRequest.getPhoneNumber() +" is already taken!");
        }

        if (signUpRequest.getEmail() != null &
                userRepository.existsByEmail(signUpRequest.getEmail().toLowerCase())) {
            throw new EmailAlreadyExistsException("Error: Email " + signUpRequest.getEmail() + " is already in use!");
           }

        // Create new user's account
        User user = new User(
                signUpRequest.getFirst_name(),
                signUpRequest.getLast_name(),
                signUpRequest.getEmail().toLowerCase(),
                signUpRequest.getPhoneNumber(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "techsupport":
                        Role techRole = roleRepository.findByName(ERole.ROLE_TECHSUPPORT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(techRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        EmailUtility.sendVerificationEmail(user, mailSender);
        userRepository.save(user);
        return user;


    }

    public boolean verifyUser(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }

    }

    public TokenRefreshResponse refreshtoken(TokenRefreshRequest request){

        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }


    public String resetPassword(String userEmail) throws MessagingException, UnsupportedEncodingException {
        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            throw new NotFoundException("User with Email " + userEmail + " not found");
        }
        String token = String.valueOf(new Random().nextInt(900000) + 100000);
        userService.createPasswordResetTokenForUser(user, token);
        EmailUtility.sendPasswordResetCode(token, user, mailSender );
        return ("Check your email");
    }

    public String changePassword(String token, String newPassword){
        PasswordResetToken passwordResetToken =
                securityService.validatePasswordResetToken(token);

        userService.changePassword(passwordResetToken, newPassword);
        return "Password changed";
    }


    public boolean verifyPasswordResetToken(String email, String token) {

        PasswordResetToken passwordResetToken =
                securityService.validatePasswordResetToken(token);

        return passwordResetToken.getUser().getEmail().equals(email);

    }

    public String getFirebaseToken() throws FirebaseAuthException {
        userService.getCurrentUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //joining elements of collections as comma seperated string
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        System.out.println(authorities);
        return firebaseAuth.createCustomToken(username, Collections.singletonMap("authorities", authorities));
    }
}







