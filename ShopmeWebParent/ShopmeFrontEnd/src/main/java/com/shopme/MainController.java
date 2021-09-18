package com.shopme;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopme.brand.BrandService;
import com.shopme.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.product.ProductService;

@Controller
public class MainController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

//    @GetMapping("")
//    public String viewHomePage(Model model) {
//	List<Category> listCategories = categoryService.listNoChildrenCategories();
//	model.addAttribute("listCategories", listCategories);
//	return "index";
//    }

    @GetMapping("/login")
    public String viewLoginPage() {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
//	    return "/login";
	    return "test/login/login";
	}
	return "redirect:/";
    }

    @GetMapping("")
    public String viewIndex(Model model) {
	List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();
	List<Product> listProductForFeatured = productService.getProductForFeatured();
	List<Product> listProductsForFlashDeal = productService.getProductForFlashDeal();
	List<Product> listProductsForGift = productService.getProductsForGift();
	List<Product> listProductForRate = productService.getProductsForRated();
	List<Brand> listBrands = brandService.listAll();
	List<Product> listProductByBrandName = productService.getProductByBrandName("asus");
	model.addAttribute("listProductByBrand", listProductByBrandName);
	model.addAttribute("listBrands", listBrands);
	model.addAttribute("listProductForFlashSale", listProductsForFlashDeal);
	model.addAttribute("listProductsForGift", listProductsForGift);
	model.addAttribute("listProductForFeatured", listProductForFeatured);
	model.addAttribute("listProductForRate", listProductForRate);
	model.addAttribute("listCategories", listCategoriesUsedInForm);
	return "test/home1";
    }

    @RequestMapping(value = "/searchJquery")
    @ResponseBody
    public List<Product> findItem(@RequestParam("term") String name) {
	return productService.findName(name);
    }

//    @GetMapping("/b/{brandName}")
//    public String viewProductByBrandName(@PathVariable("brandName") String brandName, Model model) {
//
//	List<Product> listProductByBrand = productService.listProductByBrand(brandName);
//	Brand brand = brandService.findByName(brandName);
//	model.addAttribute("listProducts", listProductByBrand);
//	model.addAttribute("brand", brand);
//	return "test/shopleft_sidebar";
//    }

    @GetMapping("/p/testquickview/{id}")
    public String quickView(@PathVariable("id") Integer productId, Model model) throws ProductNotFoundException {
	Product product = productService.get(productId);
	model.addAttribute("product", product);
	return "test/quickview";
    }
    @GetMapping("/test/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
	return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/test/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable("pageNum") int pageNum,
	    Model model) {
	try {
	    Category category = categoryService.getCategory(alias);
	    if (category == null) {
		return "error/404";
	    }

	    List<Category> listCategoryParents = categoryService.getCategoryParents(category);
	    Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
	    List<Product> listProducts = pageProducts.getContent();
	    long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
	    long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
	    if (endCount > pageProducts.getTotalElements()) {
		endCount = pageProducts.getTotalElements();
	    }
	    model.addAttribute("startCount", startCount);
	    model.addAttribute("currentPage", pageNum);
	    model.addAttribute("endCount", endCount);
	    model.addAttribute("totalPages", pageProducts.getTotalPages());
	    model.addAttribute("totalItems", pageProducts.getTotalElements());
	    model.addAttribute("pageTitle", category.getName());
	    model.addAttribute("listProducts", listProducts);
	    model.addAttribute("listCategoryParents", listCategoryParents);
	    model.addAttribute("category", category);
	    return "test/shopleft_sidebar";
	} catch (CategoryNotFoundException e) {
	    return "error/404";
	}
    }
    @GetMapping("/product/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
	try {
	    Product product = productService.getProduct(alias);
	    List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
	    model.addAttribute("listCategoryParents", listCategoryParents);
	    model.addAttribute("product", product);
	    model.addAttribute("pageTitle", product.getName());
	    return "test/simple";

	} catch (ProductNotFoundException e) {
	    return "error/404";
	}
    }
   
}
