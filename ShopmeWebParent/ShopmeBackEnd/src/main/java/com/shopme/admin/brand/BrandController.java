	package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listFirstPage(Model model) {

	return "redirect:/brands/page/1?sortField=name&sortDir=asc";

    }

    @GetMapping("/brands/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum, Model model,
	    @PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands") PagingAndSortingHelper helper) {
	brandService.listByPage(pageNum, helper);
	return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model) {
	List<Category> listCategories = categoryService.listCategoriesUsedInForm();
	model.addAttribute("listCategories", listCategories);
	model.addAttribute("brand", new Brand());
	model.addAttribute("pageTitle", "Create New Brand");

	return "brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
	    RedirectAttributes redirectAttributes) throws IOException {
	if (!multipartFile.isEmpty()) {
	    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	    brand.setLogo(fileName);
	    Brand savedBrand = brandService.save(brand);
	    String uploadDir = "../brands-logos/" + savedBrand.getId();
	    FileUploadUtil.cleanDir(uploadDir);
	    FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	} else {
	    brandService.save(brand);
	}

	redirectAttributes.addFlashAttribute("message", "The brand have been saved successfully.");
	return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
	    Model model) {
	try {
	    Brand brand = brandService.get(id);
	    List<Category> listCategories = categoryService.listCategoriesUsedInForm();
	    model.addAttribute("brand", brand);
	    model.addAttribute("listCategories", listCategories);
	    model.addAttribute("pageTitle", "Edit Brand (ID:)" + id);
	    return "brands/brand_form";
	} catch (BrandNotFoundException e) {
	    redirectAttributes.addFlashAttribute("message", e.getMessage());
	    return "redirect:/brands";
	}
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
	try {
	    brandService.delete(id);
	    String categoryDir = "../brands-logos/" + id;
	    FileUploadUtil.removeDir(categoryDir);
	    redirectAttributes.addFlashAttribute("message", "The brand ID: " + id + " has been deleted successfully");
	} catch (BrandNotFoundException e) {
	    redirectAttributes.addFlashAttribute("message", e.getMessage());
	}

	return "redirect:/brands";
    }
}
