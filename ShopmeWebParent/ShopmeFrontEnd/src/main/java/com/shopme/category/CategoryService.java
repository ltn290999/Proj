package com.shopme.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    public List<Category> listNoChildrenCategories() {
	List<Category> listNoChildrenCategories = new ArrayList<>();
	List<Category> listEnabledCategories = repo.findAllEnabled();
	listEnabledCategories.forEach(category -> {
	    Set<Category> children = category.getChildren();
	    if (children == null || children.size() == 0) {
		listNoChildrenCategories.add(category);
	    }
	});
	return listNoChildrenCategories;
    }
    
    

    public Category getCategory(String alias) throws CategoryNotFoundException {
	Category category = repo.findByAliasEnabled(alias);
	if (category == null) {
	    throw new CategoryNotFoundException("Could not find any categories with alias " + alias);
	}
	return category;
    }

    public List<Category> getCategoryParents(Category child) {
	List<Category> listParents = new ArrayList<>();
	Category parent = child.getParent();
	while (parent != null) {
	    listParents.add(0, parent);
	    parent = parent.getParent();
	}
	listParents.add(child);
	return listParents;
    }
    
    public List<Category> listCategoriesUsedInForm() {
   	List<Category> categoriesUsedInForm = new ArrayList<>();
   	Iterable<Category> categoriesInDB = repo.findRootCategories(Sort.by("name").ascending());
   	for (Category category : categoriesInDB) {
   	    if (category.getParent() == null) {
   		Set<Category> children = sortSubCategories(category.getChildren());
   		for (Category subCategory : children) {
   		    categoriesUsedInForm.add(subCategory);
   		}
   	    }
   	}

   	return categoriesUsedInForm;

       }

       private SortedSet<Category> sortSubCategories(Set<Category> children) {
   	return sortSubCategories(children, "asc");
       }

       private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
   	SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
   	    @Override
   	    public int compare(Category o1, Category o2) {
   		if (sortDir.equals("asc")) {
   		    return o1.getName().compareTo(o2.getName());
   		} else {
   		    return o2.getName().compareTo(o1.getName());
   		}
   	    }
   	});

   	sortedChildren.addAll(children);
   	return sortedChildren;

       }
}
