package com.neobis.rentit.services.impl;

import com.cloudinary.api.exceptions.NotFound;
import com.google.common.base.Joiner;
import com.neobis.rentit.controller.ProductImagesController;
import com.neobis.rentit.dto.*;
import com.neobis.rentit.exception.AttributeNotInCategoryException;
import com.neobis.rentit.exception.NotFoundException;
import com.neobis.rentit.model.*;
import com.neobis.rentit.model.enums.SearchOperation;
import com.neobis.rentit.model.enums.SortDirection;
import com.neobis.rentit.repository.*;
import com.neobis.rentit.services.ProductService;
import com.neobis.rentit.utils.SpecSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductAddressRepository productAddressRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRatingRepository productRatingRepository;
    private final AttributeServiceImpl attributeService;
    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;
    private final ProductHasAttributeServiceImpl productHasAttributeService;
    private final SavedProductsRepository savedProductsRepository;

    private final FollowerRepository followerRepository;

    private final GeoLocationRepository geoLocationRepository;


    @Override
    public List<Product> getAllProducts() {
        List<Product> pr = productRepository.findAll();
        List<Product> response = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (Product p : pr) {
            if (p.getDateDeactivated() == null || p.getDateDeactivated().isBefore(now)  ) {
                p.setIsActive(false);
                if (p.getAdvertisement()) {
                    p.setAdvertisement(false);
                }
                productRepository.save(p);
            } else response.add(p);
        }
        return response;
    }

    @Override
    public Product save(ProductSavingRequest dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("Error: User ID is not found."));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Error: Category ID is not found."));

        if (dto.getAttributeRequestList() != null) {

            List<Long> attributeIDs = dto.getAttributeRequestList().stream().map(AttributeRequest::getAttributeId)
                    .collect(Collectors.toList());

            List<Long> attributesNotInCategory = categoryService.
                    whichAttributesAreNotInCategory(attributeIDs, category.getId());

            if (!attributesNotInCategory.isEmpty()) {

                String listString = attributesNotInCategory.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));

                throw new AttributeNotInCategoryException(
                        String.format("Attributes with ids: [%s] are not related to category with id: %s",
                                listString, category.getId()));

            }
        }


        ProductAddress productAddress =
                new ProductAddress(dto.getCountry().toLowerCase(), dto.getCity().toLowerCase(), dto.getStreet().toLowerCase());

        productAddressRepository.save(productAddress);


        LocalDate deactivatedDate = LocalDate.now().plusDays(2);
        Product product = new Product(dto.getName(), dto.getPrice(), dto.getCondition(),
                dto.getDescription(), 0, Boolean.FALSE, user, productAddress, category,
                0, 0, dto.getTypeOfRental(), deactivatedDate, true);

        product = productRepository.save(product);

        if (dto.getAttributeRequestList() != null){
            List<ProductHasAttribute> productHasAttributes =
                    productHasAttributeService.saveAttributes(dto.getAttributeRequestList(), product.getId());

            product.setAttributesList(productHasAttributes);
        }
        GeoLocation geo =geoLocationRepository.save(dto.getGeoLocation());
        product.setGeoLocation(geo);


        productRepository.save(product);

        return product;
    }


    public List<Product> getAllActiveProductsBySession() {

        User user = userService.getAuthentication();
        return productRepository.findAllActiveProductsByUser(user.getId());


    }

    public Product getProductById(Long id) {

        return productRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Product with id %s not found", id)));
    }

    public ProductBasicInfo getProductDTOById(Long id) throws NotFound {
        if (getProductById(id) == null){
            throw new NotFound("Product not found");
        }
        return productToProductBasicDTO(getProductById(id));

    }


    public Set<ProductBasicInfo> getAllProductBasicDTOs() {
        List<Product> products = getAllProducts();
        Set<ProductBasicInfo> productBasicInfos = new HashSet<>();

        for (Product product : products) {
            productBasicInfos.add(productToProductBasicDTO(product));
        }
        return productBasicInfos;
    }


    public Page<Product> getAllProductsPaged(ProductPagedRequest pPR) {


        List<Sort.Order> sorts= new ArrayList<>();

        if (pPR.getSortRequests() != null){
            for(SortRequest sortRequest : pPR.getSortRequests()){
                if (sortRequest.getSortDirection() == SortDirection.ASC){
                    sorts.add(new Sort.Order(Sort.Direction.ASC, sortRequest.getFieldName()));
                }
                else{
                    sorts.add(new Sort.Order(Sort.Direction.DESC, sortRequest.getFieldName()));
                }
            }
        }

        Pageable paging = PageRequest.of(pPR.getPage().intValue(), pPR.getSize().intValue(),Sort.by(sorts));

        Page<Product> pageProduct;

        ProductSpecification advertisementSpec = new ProductSpecification(
                new SpecSearchCriteria("advertisement", SearchOperation.EQUALITY, pPR.getAd()));

        Specification<Product> specs = resolveSpecification(pPR.getSearchQuery());

        if (pPR.getCategoryId() != null){
            Set<Long> catids  = categoryService.collectLeafChildren(pPR.getCategoryId(), new HashSet<>());
            List<Category> categories = categoryService.getAllCategoriesInSet(catids);
            Specification<Product> categorySpec =  ProductSpecification.inCategoryOrSubCategory(categories);
            specs = Specification.where(specs).and(categorySpec);
        }

        if (pPR.getCity() != null){
            Specification<Product> inCity =  ProductSpecification.inCity(pPR.getCity());
            specs = Specification.where(specs).and(inCity);
        }

        pageProduct = productRepository.findAll(Specification.where(specs).and(advertisementSpec),paging);


        return pageProduct;
    }


    public Page<ProductBasicInfo> getAllProductsBasicPaged(ProductPagedRequest productPagedRequest) {

        Page<Product> products = getAllProductsPaged(productPagedRequest);
        return products.map(this::productToProductBasicDTO);

    }




    public List<ProductBasicInfo> compareListOfProducts(List<Long> productIds) throws NotFound {
        List<ProductBasicInfo> dtos = new ArrayList<>();
        for (Long id : productIds) {
            ProductBasicInfo info = getProductDTOById(id);
            dtos.add(info);
        }
        return dtos;
    }


    public List<Product> getSavedProductsBySession() {
        User user = userService.getCurrentUser();

        return productRepository.findByListOfProductIds(
                savedProductsRepository.findAllSavedProductsByUserId(user.getId()));
    }

    public List<ProductBasicInfo> getSavedBasicProductsBySession() {

        return productListToProductBasicDTOList(getSavedProductsBySession());
    }


    ProductBasicInfo productToProductBasicDTO(Product product) {

        ModelMapper modelMapper = new ModelMapper();
        ProductBasicInfo productBasicInfo = modelMapper.map(product, ProductBasicInfo.class);
        productBasicInfo.setUserid(product.getUser().getId());
        List<ProductAttributeDto> productAttributeDtos = new ArrayList<>();

        for (ProductAttributeDto productAttributeDto : productBasicInfo.getAttributesList()) {
            productAttributeDto.setAttributeName(attributeService.getAttributeName(productAttributeDto.getAttributeId()));
            productAttributeDtos.add(productAttributeDto);
        }

        Double rating = productRatingRepository.findAverageRatingByProductId(product.getId());

        productBasicInfo.setImagesList(changePathToUrl(product.getImagesList()));
        productBasicInfo.setAttributesList(productAttributeDtos);

        productBasicInfo.setRating(findRatingByAvr(rating));


        return productBasicInfo;
    }

    private List<ProductImageDto> changePathToUrl(List<ProductImages> imagesList) {

        ArrayList<ProductImageDto> productImageDtos = new ArrayList<>();
        for (ProductImages productImage: imagesList){
            String[] pathsplit = productImage.getUrl().split("[/\\\\]");
            String url = (MvcUriComponentsBuilder
                    .fromMethodName(ProductImagesController.class, "getFile",
                            pathsplit[1], pathsplit[2])).build().toString();

            productImageDtos.add(new ProductImageDto(url,pathsplit[1],pathsplit[2]));
        }
        return productImageDtos;

    }

    Double findRatingByAvr(Double avr){
        if (avr == null) {
            return 0.0;
        } else {
            return Math.round(avr * 10) / 10.0;
        }
    }


    private List<ProductBasicInfo> productListToProductBasicDTOList(List<Product> products) {

        List<ProductBasicInfo> productBasicInfos = new ArrayList<>();

        for (Product product : products) {
            productBasicInfos.add(productToProductBasicDTO(product));
        }

        return productBasicInfos;
    }


    public List<Product> findAllBySpec(String search) {
        Specification<Product> spec = resolveSpecification(search);

        return productRepository.findAll(spec);
    }

    protected Specification<Product> resolveSpecification(String searchParameters) {
        ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile(
                "(\\p{Punct}?)(\\w+?)("
                        + operationSetExper
                        + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),",Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(searchParameters + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                    matcher.group(5), matcher.group(4), matcher.group(6));
        }

        return builder.build();
    }

    List<ProductBasicInfo> getLastFourProductsByUserId(User user){
        List<ProductBasicInfo> response = new ArrayList<>();
        if(productRepository.findFourthByOrderByCreationDate(user.getId()).isEmpty()){
            return response;
        }
        List<Product> products = productRepository.findFourthByOrderByCreationDate(user.getId());
        for(Product p : products){
            response.add(productToProductBasicDTO(p));
        }
        return response;

    }

    public Product extendDeactivationTime(Long productId, Long days) {

        Product product = getProductById(productId);
        if ((product.getDateDeactivated().isBefore(LocalDate.now()))) {
            product.setDateDeactivated(LocalDate.now().plusDays(days));
        } else {
            product.setDateDeactivated(product.getDateDeactivated().plusDays(days));
        }
        product.setIsActive(true);
        productRepository.save(product);
        return product;
    }

    public Product findProductById(Long productId) {
        return  productRepository.findById(productId).
                orElseThrow(() -> new NotFoundException("Product not Found") );
    }

    public String hardDeleteById(Long id) {

        productRepository.deleteById(id);
        return "Product with id:" + id + " deleted";
    }
}


