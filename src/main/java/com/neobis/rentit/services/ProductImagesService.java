package com.neobis.rentit.services;

import com.neobis.rentit.model.ProductImages;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface ProductImagesService {

    public void init();

    public void save(MultipartFile[] file, Long productId);

    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();

    List<ProductImages> loadAllByProductId(Long productId);
}
