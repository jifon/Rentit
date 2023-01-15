package com.neobis.rentit.services.impl;

import com.neobis.rentit.controller.ProductImagesController;
import com.neobis.rentit.dto.FullRegistrationAfterAuthenticationRequest;
import com.neobis.rentit.dto.UserBasic;
import com.neobis.rentit.exception.NotAuthenticatedException;
import com.neobis.rentit.exception.NotFoundException;
import com.neobis.rentit.model.*;
import com.neobis.rentit.model.enums.ApplicationStatus;
import com.neobis.rentit.repository.*;
import com.neobis.rentit.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Path root = Paths.get("user");
    private final Path rootBanner = Paths.get("banner");


    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserAddressRepository userAddressRepository;
    private final FullRegistrationRepository fullRegistrationRepository;
    private final ProductRatingRepository productRatingRepository;


    private final PasswordEncoder encoder;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllNotDeletedUsers() {
        return userRepository.findAllNotDeletedUsers();
    }

    public User getNotDeletedUserById(Long id) {
        User user = isUserDeletedCheck(id);
        return userRepository.findNotDeletedUserById(id);
    }

    public User deleteUserById(Long id) {
        User user = isUserDeletedCheck(id);
        user.setDateDeleted(LocalDateTime.now());
        user.setEnabled(false);
        userRepository.save(user);
        return user;
    }


    public String hardDeleteAllUsers() {

        userRepository.deleteAll();
        return "All Users deleted";
    }

    public String hardDeleteById(Long id) {

        userRepository.deleteById(id);
        return "User with id:" + id + " deleted";
    }


    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName()).orElseThrow( () ->
                new NotAuthenticatedException("You are not logged in"));
    }



    @Override
    public User isUserDeletedCheck(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find user with id: " + id));
        if(user.getDateDeleted() != null) {
            throw new NotFoundException("User with id: " + id + " was deleted!");
        }
        return user;
    }

    public User getCurrentUser() {
        return getAuthentication();
    }

    public User deleteCurrentUser() {
        User user = getAuthentication();
        deleteUserById(user.getId());
        return user;

    }

    public User findUserByEmail(String userEmailorPhone) {
        return  userRepository.findByEmail(userEmailorPhone).
                orElseThrow(() -> new NotFoundException("User not Found") );
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    public void changePassword(PasswordResetToken token, String newPassword) {
        User user = token.getUser();
        user.setPassword(encoder.encode(newPassword));
        passwordResetTokenRepository.delete(token);
        userRepository.save(user);
    }

    public FullRegistration fullRegistrationAfterAuthentication(FullRegistrationAfterAuthenticationRequest request){

        User user = getAuthentication();
        UserAddress address = new UserAddress(request.getCountry(), request.getCity(), request.getStreet());
        userAddressRepository.save(address);
        FullRegistration registration = new FullRegistration( request.getPassNum(), request.getINN(),
                request.getExpDatePass(), request.getExpDatePass(), request.getIssuedAuthorityPass(),
                ApplicationStatus.INPROCESS, address, user);
        fullRegistrationRepository.save(registration);

        return registration;
    }

    public FullRegistration fullRegistration(FullRegistrationAfterAuthenticationRequest request, User user){

        UserAddress address = new UserAddress(request.getCountry(), request.getCity(), request.getStreet());
        userAddressRepository.save(address);
        FullRegistration registration = new FullRegistration( request.getPassNum(), request.getINN(),
                request.getExpDatePass(), request.getExpDatePass(), request.getIssuedAuthorityPass(),
                ApplicationStatus.INPROCESS, address, user);
        fullRegistrationRepository.save(registration);
        return registration;
    }

    public UserBasic getNotDeletedBasicUserById(Long id) {
        User user = isUserDeletedCheck(id);
        ModelMapper modelMapper = new ModelMapper();
        UserBasic userBasic =
                modelMapper.map(user, UserBasic.class);
        return userBasic;
    }

    public List<UserBasic> getAllNotDeletedBasicUsers() {

        List<UserBasic> userBasics = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();

        List<User> users = userRepository.findAllNotDeletedUsers();
        for(User user:users){

            UserBasic userBasic =
                    modelMapper.map(user, UserBasic.class);

            userBasics.add(userBasic);
        }

        return userBasics;
    }

    public Double getUserRating(User user){
        List<Double> ratingOfAllProducts = new ArrayList<>();
        List<Product> allProducts = productRepository.findAllProductsByUserId(user.getId());
        for(Product p : allProducts){
            Double rating = productRatingRepository.findAverageRatingByProductId(p.getId());
            ratingOfAllProducts.add(rating == null ? 0.0 : Math.round(rating * 10) / 10.0);

        }
        Double sum = 0.0;
        if(!ratingOfAllProducts.isEmpty()) {
            for (Double mark : ratingOfAllProducts) {
                sum += mark;
            }
            return sum / ratingOfAllProducts.size();
        }
        return sum;
    }

    public void save(MultipartFile file, Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            Files.createDirectories(Path.of(root + "/" + userId));


            Files.copy(file.getInputStream(), this.root.resolve(userId.toString()
                        +"/"
                        + file.getOriginalFilename()));
            user.get().setUsersImageUrl(this.root+"/"+ userId +"/"+file.getOriginalFilename());
            userRepository.save(user.get());

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            e.printStackTrace();

            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource loadAllByUserId(Long userId){
        try {
            Optional<User> u = userRepository.findById(userId);
            Path file = Path.of(u.get().getUsersImageUrl());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void saveBannerUrl(MultipartFile file, Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            Files.createDirectories(Path.of(rootBanner + "/" + userId));


            Files.copy(file.getInputStream(), this.rootBanner.resolve(userId.toString()
                    +"/"
                    + file.getOriginalFilename()));
            user.get().setPremiumBannerUrl(this.rootBanner+"/"+ userId +"/"+file.getOriginalFilename());
            userRepository.save(user.get());

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            e.printStackTrace();

            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource loadBannerByUserId(Long userId){
        try {
            Optional<User> u = userRepository.findById(userId);
            Path file = Path.of(u.get().getPremiumBannerUrl());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
