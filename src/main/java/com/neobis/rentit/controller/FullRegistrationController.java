package com.neobis.rentit.controller;

import com.neobis.rentit.model.ResponseMessage;
import com.neobis.rentit.model.UserImages;
import com.neobis.rentit.services.impl.FullRegistrationImagesServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/full-registration")
public class FullRegistrationController {



    @Autowired
    FullRegistrationImagesServiceImpl fullRegistrationService;



    @Operation(summary = "upload image for full registration")
    @PostMapping(value = "/upload/{fullRegistrationId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile[] file,
                                                      @PathVariable Long fullRegistrationId) {

        String message = "";
        try {
            fullRegistrationService.save(file, fullRegistrationId);
            message = "Uploaded the file successfully: ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    @GetMapping("/files-by-id/{fullRegistrationId}")
    @Operation(summary = "get all files of one full registration")
    public ResponseEntity<List<UserImages>> getFilesByFUllRegistrationId(@PathVariable Long fullRegistrationId) {
        return ResponseEntity.ok(fullRegistrationService.loadAllByFullRegistrationId(fullRegistrationId));
    }

    @GetMapping("/files/{fullRegistrationId}/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable Long fullRegistrationId, @PathVariable String filename) {
        Resource file = fullRegistrationService.load(fullRegistrationId.toString() + "/" + filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
