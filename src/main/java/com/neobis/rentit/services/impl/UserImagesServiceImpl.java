package com.neobis.rentit.services.impl;

import com.neobis.rentit.model.FullRegistration;
import com.neobis.rentit.model.UserImages;
import com.neobis.rentit.repository.FullRegistrationRepository;
import com.neobis.rentit.repository.UserImagesRepository;
import com.neobis.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserImagesServiceImpl{

//    private final String CLOUDINARY_URL =
//            "cloudinary://384959785315984:5FMydF7PW2Vt1bme_6bVttXHuOg@dhy7ofxq9";

    private final String PATH = System.getProperty("user.dir");
    private final UserRepository userRepository;
    private final FullRegistrationRepository fullRegistrationRepository;
    private final UserImagesRepository userImagesRepository;

    public byte[] downloadImage(Long imgId) throws IOException{
        Optional<UserImages> imageObject = userImagesRepository.findById(imgId);
        String fullPath = imageObject.get().getUrl();
        return Files.readAllBytes(new File(fullPath).toPath());
    }


    public String saveImages(MultipartFile[] file, Long fullRegistrationId) throws IOException {

        FullRegistration fullRegistration = fullRegistrationRepository.findById(fullRegistrationId).orElseThrow();

        List<UserImages> images = fullRegistration.getImagesList();

        for(MultipartFile f : file){

            UserImages userImages = new UserImages();
            userImages.setFullRegistration(fullRegistration);
            userImages.setUrl(saveImage(f));
            images.add(userImages);
        }
        fullRegistrationRepository.save(fullRegistration);
        return "Images saved";
    }

    public String saveImage(MultipartFile file) throws IOException {
        File convFile = new File(PATH + "/src/main/resources/images/user/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile.getPath();

//        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
//
//        File saveFile = Files.createTempFile(
//                        System.currentTimeMillis() + "",
//                        Objects.requireNonNull
//                                        (file.getOriginalFilename(), "File must have an extension")
//                                .substring(file.getOriginalFilename().lastIndexOf("."))
//                )
//                .toFile();
//
//        file.transferTo(saveFile);
//
//        Map upload = cloudinary.uploader().upload(saveFile, ObjectUtils.emptyMap());

//        return (String)upload.get("url");

    }


}