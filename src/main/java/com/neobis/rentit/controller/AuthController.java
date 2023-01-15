package com.neobis.rentit.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.firebase.auth.FirebaseAuthException;
import com.neobis.rentit.dto.FullRegistrationAfterAuthenticationRequest;
import com.neobis.rentit.dto.FullRegistrationWithSingUpDto;
import com.neobis.rentit.model.FullRegistration;
import com.neobis.rentit.model.User;
import com.neobis.rentit.payload.request.LoginRequest;
import com.neobis.rentit.payload.request.SignupRequest;
import com.neobis.rentit.payload.request.TokenRefreshRequest;
import com.neobis.rentit.payload.response.MessageResponse;
import com.neobis.rentit.payload.response.TokenRefreshResponse;
import com.neobis.rentit.repository.UserRepository;
import com.neobis.rentit.services.impl.AuthServiceImpl;
import com.neobis.rentit.services.impl.UserImagesServiceImpl;
import com.neobis.rentit.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthServiceImpl authService;

    private final UserServiceImpl userService;

    private final UserImagesServiceImpl userImagesService;

    private final UserRepository userRepository;



    @GetMapping("/firebasetoken")
    public Map<String, String> getToken() throws FirebaseAuthException {
        String customToken = authService.getFirebaseToken();
        return Collections.singletonMap("token", customToken);
    }




    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }


    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest)
            throws MessagingException, UnsupportedEncodingException {
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("Check your email"));
    }


    @GetMapping("/verifyUser")
    public ResponseEntity<MessageResponse> verifyUser(@Param("code") String code) {
        if (authService.verifyUser(code)) {
            return ResponseEntity.ok(new MessageResponse("verify_success"));
        } else {
            return ResponseEntity.ok(new MessageResponse("verify_fail"));
        }
    }

    @GetMapping("/verifyPasswordResetToken")
    public ResponseEntity<MessageResponse> verifyPasswordResetToken(@Param("email") String email,
                                            @Param("code") String token) {

        return authService.verifyPasswordResetToken(email,token)?
                ResponseEntity.ok(new MessageResponse(Boolean.TRUE.toString()))
                :ResponseEntity.ok(new MessageResponse(Boolean.FALSE.toString()));

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshtoken(request));
    }


    @PostMapping("/full-registration-apl") //после неполгной регистрации
    public ResponseEntity<FullRegistration> fullRegistrationAfterAuthentication(@RequestBody FullRegistrationAfterAuthenticationRequest request)  {
        return ResponseEntity.ok(userService.fullRegistrationAfterAuthentication(request));
    }

    @PostMapping("/tokensignin")
    public ResponseEntity<?> tokensignin(String idTokenString) throws GeneralSecurityException, IOException {

        String CLIENT_ID = "473751588677-i67i67lteojk5c359goqc6f3i4t4iiri.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();


        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            // ...
            if(userRepository.existsByGoogleId(userId)){
                System.out.println(userId);

            }
            return ResponseEntity.ok(name);

        } else {
            System.out.println("Invalid ID token.");
            return ResponseEntity.ok("Invalid ID token.");
        }


    }


    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "renitt3test@gmail.com";
        String senderName = "rentit";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";
    }


    @PostMapping("/full-registration") //первая регистрация
    public ResponseEntity<FullRegistration> fullRegistrationAfterAuthentication(@RequestBody FullRegistrationWithSingUpDto application)  throws MessagingException, UnsupportedEncodingException {

        SignupRequest signUpRequest = new SignupRequest(application.getFirst_name(),application.getLast_name(), application.getPhoneNumber(), application.getEmail(), application.getRole(), application.getPassword());
        User user = authService.registerUser(signUpRequest);

        FullRegistrationAfterAuthenticationRequest newApplication = new FullRegistrationAfterAuthenticationRequest(
                application.getPassNum(), application.getINN(), application.getIssuedDatePass(),
                application.getExpDatePass(), application.getIssuedAuthorityPass(), application.getCountry(),
                application.getCity(), application.getStreet());

        return ResponseEntity.ok(userService.fullRegistration(newApplication, user));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<MessageResponse> resetPassword(@RequestParam("userEmail") String userEmail) {
        try {
            return ResponseEntity.ok(new MessageResponse(authService.resetPassword(userEmail)));
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<MessageResponse> changePassword(@RequestParam("token") String token, String newPassword) {
        return ResponseEntity.ok(new MessageResponse(authService.changePassword(token, newPassword)));

    }


}