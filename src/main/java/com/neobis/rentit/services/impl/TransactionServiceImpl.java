package com.neobis.rentit.services.impl;

import com.neobis.rentit.controller.ProductImagesController;
import com.neobis.rentit.controller.TransactionController;
import com.neobis.rentit.dto.*;
import com.neobis.rentit.model.*;
import com.neobis.rentit.model.enums.Duration;
import com.neobis.rentit.model.enums.TariffType;
import com.neobis.rentit.payload.response.MessageResponse;
import com.neobis.rentit.repository.ProductRepository;
import com.neobis.rentit.repository.TariffRepository;
import com.neobis.rentit.repository.TransactionRepository;
import com.neobis.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ProductServiceImpl productService;
    private final ProductRepository productRepository;
    private final TariffRepository tariffRepository;

    public MessageResponse addProductWithTariff(NewProductResponse request)  {

        User user = userService.getAuthentication();
        Tariff tariff = tariffRepository.findById(request.getTariffId()).orElseThrow();
        if(payViaBalance(tariff.getPrice(), user)){
            ModelMapper modelMapper = new ModelMapper();
            ProductSavingRequest savingRequest = modelMapper.map(request, ProductSavingRequest.class);
            Product product = productService.save(savingRequest);
            product.setIsActive(true);
            productRepository.save(product);
            installTariff(tariff,product);
            Transaction transaction = new Transaction(product, product.getUser(), tariff,
                    tariff.getPrice());
            transactionRepository.save(transaction);
            return new MessageResponse("The tariff has been successfully installed");
        }else return new MessageResponse("You have errors when debiting payments via the balance." +
                " Please check your balance and try again");
    }

    public MessageResponse addTariffToProduct(TariffRequest request)  {

        User user = userService.getAuthentication();
        Tariff tariff = tariffRepository.findById(request.getTariffId()).orElseThrow();
        if(payViaBalance(tariff.getPrice(), user)){
            Product product = productService.findProductById(request.getProductId());
            product.setIsActive(true);
            productRepository.save(product);
            installTariff(tariff,product);
            Transaction transaction = new Transaction(product, product.getUser(), tariff,
                    tariff.getPrice());
            transactionRepository.save(transaction);
            return new MessageResponse("The tariff has been successfully installed");
        }else return new MessageResponse("You have errors when debiting payments via the balance." +
                " Please check your balance and try again");
    }

    public Boolean payViaBalance(Double amount, User user){
        if(user.getBalance() == 0 || user.getBalance() == null || user.getBalance() < amount){
            return false;
        }else{
            user.setBalance(user.getBalance() - amount);
            userRepository.save(user);
            return true;
        }
    }

    public void installTariff(Tariff tariff, Product product){
        User user = userService.getAuthentication();
        if(tariff.getTariffType() == TariffType.BANNER){
            user.setPremiumSubscription(true);
            user.setPremiumEndTime(findEndDate(tariff.getDuration()));
            user.setPremiumProductId(product.getId());
            List<Product> productList = productRepository.findAllProductsByUserId(user.getId());
            for(Product p : productList){
                p.setDateDeactivated(findEndDate(tariff.getDuration()));
                p.setIsActive(true);
                productRepository.save(p);
            }
            userRepository.save(user);
        }else{
            product.setAdvertisement(true);
            product.setDateDeactivated(findEndDate(tariff.getDuration()));
            product.setIsActive(true);
            productRepository.save(product);
        }

    }

    public MessageResponse putUnitsOnTheBalance(Double amount){
        User user = userService.getAuthentication();
        user.setBalance(user.getBalance()+amount);
        userRepository.save(user);
        return new MessageResponse("The balance has been successfully replenished");
    }

    public LocalDate findEndDate(Duration duration){
        LocalDate now = LocalDate.now();
        LocalDate endDate;
        if(duration == Duration.THREEDAYS){
           endDate = now.plusDays(3);
        }else if(duration == Duration.WEEK){
            endDate = now.plusWeeks(1);
        }else{
            endDate = now.plusMonths(1);
        }
        return  endDate;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Set<BannerInfoDto> getAllBanners() {
        System.out.println("0");

        List<User> allPremiumUsers = userRepository.findAllBannerUsers(LocalDate.now());

        Set<BannerInfoDto> response = new HashSet<>();
        for(User u : allPremiumUsers){

            BannerInfoDto dto = new BannerInfoDto();

            dto.setBannerUrl(MvcUriComponentsBuilder.fromMethodName
                    (TransactionController.class, "getBannerFile", u.getId()).build().toString());

            dto.setRelatedProductId(u.getPremiumProductId());

            response.add(dto);

        }

        return response;
    }


    public Set<ProductBasicInfo> getAllUrgent(){

        List<Product> allActiveProducts = productService.getAllProducts();
        Set<ProductBasicInfo> productBasicInfos = new HashSet<>();

        for (Product p : allActiveProducts){
            if ( p.getAdvertisement()){
                ProductBasicInfo basicDto = productService.productToProductBasicDTO(p);
                List<ProductImages> imagesDto = productService.getProductById(p.getId()).getImagesList();
                List<ProductImageDto> imgDtoToBasicProduct = new ArrayList<>();
                for(ProductImages pr : imagesDto){
                    ProductImageDto newImageDto = new ProductImageDto();
                    newImageDto.setName(pr.getName());
                    newImageDto.setUrl((MvcUriComponentsBuilder.fromMethodName
                            (ProductImagesController.class, "getFile", p.getId() , pr.getName()).build().toString()));
                    newImageDto.setPath(pr.getUrl());
                    imgDtoToBasicProduct.add(newImageDto);
                }
                basicDto.setImagesList(imgDtoToBasicProduct);
                productBasicInfos.add(basicDto);
            }
        }
        return productBasicInfos;
    }

    public String hardDeleteById(Long id) {

        transactionRepository.deleteById(id);
        return "Transaction with id:" + id + " deleted";
    }

}



