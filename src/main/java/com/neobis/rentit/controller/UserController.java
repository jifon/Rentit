package com.neobis.rentit.controller;

import com.neobis.rentit.dto.UserBasic;
import com.neobis.rentit.model.ProductImages;
import com.neobis.rentit.model.ResponseMessage;
import com.neobis.rentit.model.User;
import com.neobis.rentit.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;



    @Operation(summary = "Get all not deleted users")
    @GetMapping("/all-not-deleted")
    ResponseEntity<?> getAllNotDeletedUsers() {
        return ResponseEntity.ok(userService.getAllNotDeletedUsers());
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @Operation(summary = "Get not deleted user by id")
    @GetMapping("/{id}")
    ResponseEntity<?> getNotDeletedUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getNotDeletedUserById(id));
    }

    @Operation(summary = "Get not deleted basic user dto by id")
    @GetMapping("/basic/{id}")
    ResponseEntity<UserBasic> getNotDeletedBasicUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getNotDeletedBasicUserById(id));
    }

    @Operation(summary = "Get current user")
    @GetMapping("/my")
    ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(summary = "Get current user")
    @DeleteMapping("/my")
    ResponseEntity<?> deleteCurrentUser() {
        User user = userService.deleteCurrentUser();

        return ResponseEntity.ok("user with id " + user.getId() + " deleted");
    }

    @Operation(summary = "Get all users")
    @GetMapping("/all")
    ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Hard delete all users")
    @DeleteMapping("/hard-delete-all")
    ResponseEntity<?> hardDeleteAllUsers() {
        return ResponseEntity.ok(userService.hardDeleteAllUsers());
    }

    @Operation(summary = "Hard delete user by id")
    @DeleteMapping("/hard-delete/{id}")
    ResponseEntity<?> hardDeleteUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.hardDeleteById(id));
    }

    @PostMapping(value = "/upload/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file,
                                                      @PathVariable Long userId) {

        String message = "";
        try {
            userService.save(file, userId);
            message = "Uploaded the file successfully: ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/file/{userId}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable Long userId) {
        Resource file = userService.loadAllByUserId(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


}
