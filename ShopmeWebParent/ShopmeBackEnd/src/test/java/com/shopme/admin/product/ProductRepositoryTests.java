package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct() {
	Brand brand = entityManager.find(Brand.class, 38);
	Category category = entityManager.find(Category.class, 6);
	Product product = new Product();
	product.setName("Acer Insprion 300");
	product.setAlias("acer");
	product.setShortDescription("A good smart phone from Samsung");
	product.setFullDescription("This is a very good smartphone full description");

	product.setBrand(brand);
	product.setCategory(category);
	product.setPrice(456);
	product.setEnabled(true);
	product.setInStock(true);
	product.setCreatedTime(new Date());
	product.setUpdatedTime(new Date());
	Product savedProduct = repo.save(product);
	assertThat(savedProduct).isNotNull();
	assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByBrandName() {
	Brand brand = entityManager.find(Brand.class, 6);
	String name = brand.getName();
	List<Product> findByBrandName = (List<Product>) repo.findByName(name);
	findByBrandName.forEach(pro -> System.out.println(pro.getName()));
	assertThat(findByBrandName).isNotNull();
    }
}
