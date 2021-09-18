package com.shopme.product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 9;
    public static final int SEARCH_RESULTS_PER_PAGE = 9;
    @Autowired
    private ProductRepository repo;

    public Page<Product> listByCategory(int pageNum, Integer categoryId) {
	String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
	Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
	return repo.listByCategory(categoryId, categoryIdMatch, pageable);
    }
    public Page<Product> listByBrand(int pageNum, String brandName) {
  	Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
  	return repo.listByBrand(brandName, pageable);
      }

    public Product getProduct(String alias) throws ProductNotFoundException {
	Product product = repo.findByAlias(alias);
	if (product == null) {
	    throw new ProductNotFoundException("Could not find any product with alias " + alias);
	}
	return product;
    }

    public Page<Product> search(String keyword, int pageNum) {

	Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
	return repo.search(keyword, pageable);
    }

 
    public List<Product> getProductForFlashDeal() {
	Page<Product> listProductsForFlashDeal = repo
		.getProductForFlashDeal(PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "updatedTime")));
	return listProductsForFlashDeal.getContent();
    }
    

    public List<Product> getProductForFeatured() {
 	Page<Product> page = repo.findAll(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "price")));
 	return page.getContent();
     }


    public List<Product> getProductsForRated() {
	Page<Product> listProductsForRate =repo.findAll(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "updatedTime")));
	return listProductsForRate.getContent();
    }
    public List<Product> getProductsForGift() {
 	Page<Product> listProductsForGift =repo.findAll(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "length")));
 	return listProductsForGift.getContent();
     }

    public List<Product> getProductByBrandName(String name) {
	Page<Product> productByBrand = repo.getProductByBrand(name,
		PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "price")));
	return productByBrand.getContent();
    }

    public List<Product> findName(String name) {
	List<Product> listProducts = repo.findByNameSearch(name);
	List<Product> listResult = new ArrayList<>();
	for (Product product : listProducts) {
	    Product p = new Product();
	    p.setId(product.getId());
	    p.setAlias(product.getAlias());
	    p.setName(product.getName());
	    p.setMainImage(product.getMainImage());
	    listResult.add(p);
	}
	return listResult;

    }

    public List<Product> listProductByBrand(String brandName) {
	return repo.findByBrandName(brandName);
    }

    public Product get(Integer id) throws ProductNotFoundException {
	try {
	    return repo.findById(id).get();
	} catch (NoSuchElementException e) {
	    throw new ProductNotFoundException("Cound not find any product with ID: " + id);
	}
    }

}
