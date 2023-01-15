package com.neobis.rentit.services.impl;

import com.neobis.rentit.controller.ProductImagesController;
import com.neobis.rentit.model.Product;
import com.neobis.rentit.model.ProductImages;
import com.neobis.rentit.repository.ProductImagesRepository;
import com.neobis.rentit.services.ProductImagesService;
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
import java.util.stream.Stream;


@Service
public class ProductImagesServiceImpl implements ProductImagesService {

    private final Path root = Paths.get("product");

    private final ProductServiceImpl productService;
    private final ProductImagesRepository productImagesRepository;

    public ProductImagesServiceImpl(ProductServiceImpl productService, ProductImagesRepository productImagesRepository) {
        this.productService = productService;
        this.productImagesRepository = productImagesRepository;
        init();
    }


    @Override
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


    public List<ProductImages> loadAllByProductId(Long productId){
        List<ProductImages> response = productService.getProductById(productId).getImagesList();
        for(ProductImages pr : response){
            pr.setUrl(MvcUriComponentsBuilder.fromMethodName
                    (ProductImagesController.class, "getFile", productId , pr.getName()).build().toString());
        }

        return response;
    }


    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root).filter(path -> !path.equals(this.root)).map
                    (this.root::relativize).filter(path -> path.toString().contains("\\") || path.toString().contains("/"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }


    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile[] file, Long productId) {
        try {
            Product product = productService.getProductById(productId);
            Files.createDirectories(Path.of(root + "/" + productId));
            for(MultipartFile f : file){

                Files.copy(f.getInputStream(), this.root.resolve(productId.toString()
                        +"/"
                        + f.getOriginalFilename()));
                ProductImages image = new ProductImages();
                image.setProduct(product);
                image.setName(f.getOriginalFilename());
                image.setUrl(this.root+"/"+ productId +"/"+f.getOriginalFilename());
                productImagesRepository.save(image);
            }
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            e.printStackTrace();

            throw new RuntimeException(e.getMessage());
        }
    }


}
