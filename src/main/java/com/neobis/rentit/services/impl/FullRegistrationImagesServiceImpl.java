package com.neobis.rentit.services.impl;

import com.cloudinary.api.exceptions.NotFound;
import com.neobis.rentit.controller.FullRegistrationController;
import com.neobis.rentit.controller.ProductImagesController;
import com.neobis.rentit.model.FullRegistration;
import com.neobis.rentit.model.UserImages;
import com.neobis.rentit.repository.FullRegistrationRepository;
import com.neobis.rentit.repository.UserImagesRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FullRegistrationImagesServiceImpl {

    private final Path root = Paths.get("pass");


    private final FullRegistrationRepository fullRegistrationRepository;

    private final FullRegistrationServiceImpl fullRegistrationService;

    private final UserImagesRepository userImagesRepository;



    public FullRegistrationImagesServiceImpl(FullRegistrationRepository fullRegistrationRepository, FullRegistrationServiceImpl fullRegistrationService, UserImagesRepository userImagesRepository) {
        this.fullRegistrationRepository = fullRegistrationRepository;
        this.fullRegistrationService = fullRegistrationService;
        this.userImagesRepository = userImagesRepository;
        init();
    }


    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
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


    public List<UserImages> loadAllByFullRegistrationId(Long fullRegistrationId){
        List<UserImages> response = fullRegistrationService.getFullRegistrationById(fullRegistrationId).getImagesList();
        for(UserImages pr : response){
            pr.setUrl(MvcUriComponentsBuilder.fromMethodName
                    (FullRegistrationController.class, "getFile", fullRegistrationId, pr.getName()).build().toString());
        }

        return response;
    }



    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }



    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public void save(MultipartFile[] file, Long fullRegistrationId) {
        try {
            Optional<FullRegistration> fullRegistration = fullRegistrationRepository.findById(fullRegistrationId);
            Files.createDirectories(Path.of(root + "/" + fullRegistrationId));

            if(fullRegistration.isPresent()){
                for(MultipartFile f : file){
                    Files.copy(f.getInputStream(), this.root.resolve(fullRegistrationId.toString()
                            + "/"
                            + f.getOriginalFilename()));
                    UserImages image = new UserImages();
                    image.setFullRegistration(fullRegistration.get());
                    image.setName(f.getOriginalFilename());
                    image.setUrl(this.root+"/"+f.getOriginalFilename());
                    userImagesRepository.save(image);
                }
            }else throw new NotFound("Registration not found");
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            e.printStackTrace();

            throw new RuntimeException(e.getMessage());
        }
    }

}
