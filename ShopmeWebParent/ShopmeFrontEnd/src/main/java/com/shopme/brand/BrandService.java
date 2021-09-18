package com.shopme.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
public class BrandService {
  
    @Autowired
    private BrandRepository repo;

    public List<Brand> listAll() {
	return (List<Brand>) repo.findAll();
    }
    
    public Brand findByName(String name) {
	return repo.findByName(name);
    }
    
    public List<Brand> listBrandForBanner(){
	Page<Brand> page = repo.findAll(PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "name")));
	return page.getContent();
    }
    
}
