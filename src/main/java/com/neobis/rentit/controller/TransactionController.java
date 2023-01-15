package com.neobis.rentit.controller;

import com.neobis.rentit.dto.BannerInfoDto;
import com.neobis.rentit.dto.NewProductResponse;
import com.neobis.rentit.dto.ProductBasicInfo;
import com.neobis.rentit.dto.TariffRequest;
import com.neobis.rentit.model.ResponseMessage;
import com.neobis.rentit.model.Transaction;
import com.neobis.rentit.payload.response.MessageResponse;
import com.neobis.rentit.services.impl.TransactionServiceImpl;
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
import java.util.Set;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionServiceImpl transactionService;
    private final UserServiceImpl userService;

    @PostMapping("/add-tariff-and-product")
    @Operation(summary = "adding new product with tariff")
    public ResponseEntity<MessageResponse> addTariff(@RequestBody NewProductResponse request)  {
        return ResponseEntity.ok(transactionService.addProductWithTariff(request));
    }

    @PostMapping("/add-tariff-to-product")
    @Operation(summary = "adding tariff to existing product")
    public ResponseEntity<MessageResponse> addTariffToExistingProduct(@RequestBody TariffRequest request)  {
        return ResponseEntity.ok(transactionService.addTariffToProduct(request));
    }

    @Operation(summary = "Get full information about the tariff users")
    @GetMapping("/get-all")
    ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping("/pay-balance")
    public ResponseEntity<MessageResponse> addBalance(@RequestBody Double amount)  {
        return ResponseEntity.ok(transactionService.putUnitsOnTheBalance(amount));
    }

    @PostMapping(value = "/upload-banner/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file,
                                                      @PathVariable Long userId) {

        String message = "";
        try {
            userService.saveBannerUrl(file, userId);
            message = "Uploaded the file successfully: ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    //все срочные
    @Operation(summary = "Get all urgent products")
    @GetMapping("/get-all-urgent")
    ResponseEntity<Set<ProductBasicInfo>> getAllUrgent() {
        return ResponseEntity.ok(transactionService.getAllUrgent());
    }

    //все баннеры
    @Operation(summary = "Get all banners and their information")
    @GetMapping("/get-all-banner")
    ResponseEntity<Set<BannerInfoDto>> getAllBanners() {
        return ResponseEntity.ok(transactionService.getAllBanners());
    }


    @GetMapping("/file-banner/{userId}")
    @ResponseBody
    public ResponseEntity<Resource> getBannerFile(@PathVariable Long userId) {
        Resource file = userService.loadBannerByUserId(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @Operation(summary = "Hard delete transaction by id")
    @DeleteMapping("/hard-delete/{id}")
    ResponseEntity<?> hardDeleteTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.hardDeleteById(id));
    }




}
