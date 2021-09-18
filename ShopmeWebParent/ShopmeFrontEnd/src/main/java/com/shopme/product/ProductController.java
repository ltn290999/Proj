package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.brand.BrandService;
import com.shopme.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
	return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable("pageNum") int pageNum,
	    Model model) {
	try {
	    Category category = categoryService.getCategory(alias);
	    if (category == null) {
		return "error/404";
	    }

	    List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();
	    List<Brand> listBrandForBanner = brandService.listBrandForBanner();
	    model.addAttribute("listBrandForBanner", listBrandForBanner);
	    model.addAttribute("listCategories", listCategoriesUsedInForm);
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
//	    return "product/products_by_category";
	    return "test/product/shopleft_sidebar";
	} catch (CategoryNotFoundException e) {
	    return "error/404";
	}
    }

    @GetMapping("/b/{brand_name}")
    public String viewBrandFirstPage(@PathVariable("brand_name") String brand_name, Model model) {
	return viewBrandByPage(brand_name, 1, model);
    }

    @GetMapping("/b/{brand_name}/page/{pageNum}")
    public String viewBrandByPage(@PathVariable("brand_name") String brandName, @PathVariable("pageNum") int pageNum,
	    Model model) {

	List<Category> listCategoriesUsedInForm = categoryService.listCategoriesUsedInForm();
	List<Brand> listBrandForBanner = brandService.listBrandForBanner();
	model.addAttribute("listBrandForBanner", listBrandForBanner);
	model.addAttribute("listCategories", listCategoriesUsedInForm);
	model.addAttribute("brandName", brandName);
	Page<Product> pageProducts = productService.listByBrand(pageNum, brandName);
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
	model.addAttribute("listProducts", listProducts);
//	    return "product/products_by_category";
	return "test/product/shopleft_sidebar";
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
	try {
	    Product product = productService.getProduct(alias);
	    List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
	    model.addAttribute("listCategoryParents", listCategoryParents);
	    model.addAttribute("product", product);
	    model.addAttribute("pageTitle", product.getName());
//	    return "product/product_detail";
	    return "test/product/simple";
	} catch (ProductNotFoundException e) {
	    return "error/404";
	}
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword, Model model) {
	return search(keyword, 1, model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String search(@Param("keyword") String keyword, @PathVariable("pageNum") int pageNum, Model model) {
	Page<Product> pageProducts = productService.search(keyword, pageNum);
	List<Product> listResult = pageProducts.getContent();
	model.addAttribute("listResult", listResult);
	model.addAttribute("keyword", keyword);
	long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
	long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
	if (endCount > pageProducts.getTotalElements()) {
	    endCount = pageProducts.getTotalElements();
	}
	model.addAttribute("startCount", startCount);
	model.addAttribute("currentPage", pageNum);
	model.addAttribute("endCount", endCount);
	model.addAttribute("totalPages", pageProducts.getTotalPages());
	model.addAttribute("totalItems", pageProducts.getTotalElements());
	model.addAttribute("pageTitle", keyword + " - Search Result");

	return "product/search_result";
    }
}
