package com.neobis.rentit;

import com.neobis.rentit.model.Product;
import com.neobis.rentit.model.ProductSpecification;
import com.neobis.rentit.model.User;
import com.neobis.rentit.model.enums.SearchOperation;
import com.neobis.rentit.repository.ProductRatingRepository;
import com.neobis.rentit.repository.ProductRepository;
import com.neobis.rentit.repository.UserRepository;
import com.neobis.rentit.services.impl.ProductImagesServiceImpl;
import com.neobis.rentit.services.impl.ProductServiceImpl;
import com.neobis.rentit.utils.SpecSearchCriteria;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class RentitApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private ProductRatingRepository productRatingRepository;

	@Autowired
	private ProductServiceImpl productService;

	@Autowired
	private ProductImagesServiceImpl productImagesService;

	@Test
	public void givenAgeRange_whenGettingListOfUsers_thenCorrect() {
		ProductSpecification spec = new ProductSpecification(
				new SpecSearchCriteria("id", SearchOperation.GREATER_THAN, "20"));
		ProductSpecification spec1 = new ProductSpecification(
				new SpecSearchCriteria("id", SearchOperation.LESS_THAN, "25"));

		List<Product> results = productRepository.findAll(Specification.where(spec).and(spec1));

		System.out.println(results);
	}

	@Test
	public void ad() {

		ProductSpecification spec = new ProductSpecification(
				new SpecSearchCriteria("advertisement", SearchOperation.EQUALITY, true));

		List<Product> results = productRepository.findAll(Specification.where(spec));

		System.out.println(results.stream().map(Product::getId).collect(Collectors.toList()));
	}

	@Test
	public void loadAllTest() {

		Stream<Path> all = productImagesService.loadAll();
		System.out.println(all.collect(Collectors.toList()));

	}

	@Test
	public void testReviewCount() {

		List<Product> products = productRepository.findAll();

		for(Product product: products){
			System.out.println(productRatingRepository.countByProduct(product) + " product id:"+product.getId().toString());
//			product.setNumberOfReviews(productRatingRepository.countByProduct(product));
//			productRepository.save(product);
		}



	}

	@Test
	public void findWeightedAverageOfUser() {

		List<User> users = userRepository.findAll();

		for(User user: users){
			System.out.println(userRepository.findAverageRatingOfUserForProductsByUserId(user.getId()) +
					" --- user id:"+user.getId().toString());
			user.setRating(userRepository.findAverageRatingOfUserForProductsByUserId(user.getId()));
			userRepository.save(user);
		}



	}

}
