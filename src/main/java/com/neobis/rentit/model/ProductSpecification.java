package com.neobis.rentit.model;

import com.neobis.rentit.utils.SpecSearchCriteria;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private SpecSearchCriteria criteria;

    public static Specification<Product> inCategoryOrSubCategory(List<Category> categories) {
        return (productRoot, query, criteriaBuilder) -> criteriaBuilder.in(productRoot.get("category")).value(categories);

    }

    public static Specification<Product> inCity(String city) {

        return (productRoot, query, criteriaBuilder) -> {
            Join<ProductAddress, Product> productAddressProductJoin = productRoot.join("address");
            return criteriaBuilder.like(productAddressProductJoin.get("city"),"%" + city.toLowerCase() + "%" );
        };


    }

//    Join<Category, Product> productCategory = root.join("category");
//            return criteriaBuilder.isMember(productCategory.get("category.id"), );


    @Override
    public Predicate toPredicate(
            Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.<String> get(
                        criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.<String> get(
                        criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(root.<String> get(
                        criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.<String> get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.<String> get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.<String> get(
                        criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }


}