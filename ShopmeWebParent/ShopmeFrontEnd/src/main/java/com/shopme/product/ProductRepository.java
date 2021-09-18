package com.shopme.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.product.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 OR p.category.allParentIds LIKE %?2%) ORDER BY p.name ASC")
    public Page<Product> listByCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.brand.name = ?1")
    public Page<Product> listByBrand(String brandName, Pageable pageable);
    
    public Product findByAlias(String alias);

    @Query(value = "SELECT * FROM products p WHERE p.enabled = true AND MATCH(name,short_description,full_description) AGAINST(?1)  ", nativeQuery = true)
    public Page<Product> search(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.discountPercent !=0")
    public Page<Product> getProductForFlashDeal(Pageable pageable);
    
  

    @Query("SELECT p FROM Product p WHERE p.brand.name = ?1 ")
    public Page<Product> getProductByBrand(String brandName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    public List<Product> findByNameSearch(String name);
    
    @Query("SELECT p FROM Product p WHERE p.brand.name = ?1")
    public List<Product> findByBrandName(String brandName);
    
}
