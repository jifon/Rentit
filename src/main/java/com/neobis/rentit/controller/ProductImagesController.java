package com.neobis.rentit.controller;

import com.neobis.rentit.model.ProductImages;
import com.neobis.rentit.model.ResponseMessage;
import com.neobis.rentit.services.ProductImagesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/product-images")
public class ProductImagesController {


    private final ProductImagesService productImagesService;

    @PostMapping(value = "/upload/{productId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile[] file,
                                                      @PathVariable Long productId) {

        String message;
        try {
            productImagesService.save(file, productId);
            message = "Uploaded the file successfully: ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProductImages>> getListFiles() {
        List<ProductImages> fileInfos = productImagesService.loadAll().map(path -> {

            String[] productPathProductImageName = path.toString().split("[/\\\\]");
            String url = (MvcUriComponentsBuilder
                    .fromMethodName(ProductImagesController.class, "getFile",
                            productPathProductImageName[0], productPathProductImageName[1])).build().toString();

            return new ProductImages(url, productPathProductImageName[1]);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files-by-product/{productId}")
    @Operation(summary = "get all files of one product")
    public ResponseEntity<List<ProductImages>> getFilesByProductsId(@PathVariable Long productId) {
        return ResponseEntity.ok(productImagesService.loadAllByProductId(productId));
    }

    @GetMapping("/files/{productid}/{name}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable Long productid, @PathVariable String name) {
        Resource file = productImagesService.load(productid.toString() +"/"+ name);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
