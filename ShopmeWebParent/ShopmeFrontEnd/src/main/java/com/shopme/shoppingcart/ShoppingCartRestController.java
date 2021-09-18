package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@RestController
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable("productId") Integer productId,
	    @PathVariable("quantity") Integer quantity, HttpServletRequest request) {
	try {
	    Customer customer = getAuthenticatedCustomer(request);
	    Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);

	    return updatedQuantity + " item(s) of this product were added to your shopping cart.";
	} catch (CustomerNotFoundException e) {
	    return "You must login to add this product to cart.";
	} catch (ShoppingCartException e) {
	    return e.getMessage();
	}
    }

    @GetMapping("/cart/count")
    public int countItems(HttpServletRequest request) throws CustomerNotFoundException {
	Customer customer = null;
	try {
	    customer = getAuthenticatedCustomer(request);
	} catch (Exception e) {
	    return 0;
	}
	List<CartItem> cartItems = cartService.listCartItems(customer);
	int count = 0;
	for (CartItem cartItem : cartItems) {
	    count += cartItem.getQuantity();
	}
	return count;
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
	String email = Utility.getEmailOfAuthenticatedCustomer(request);
	if (email == null) {
	    throw new CustomerNotFoundException("No authenticated customer");
	}
	return customerService.getCustomerByEmail(email);
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Integer productId,
	    @PathVariable("quantity") Integer quantity, HttpServletRequest request) {
	try {
	    Customer customer = getAuthenticatedCustomer(request);
	    float subtotal = cartService.updateQuantity(productId, quantity, customer);
	    return String.valueOf(subtotal);
	} catch (CustomerNotFoundException e) {
	    return "You must login to change quantity of product.";
	}

    }

    @DeleteMapping("/cart/remove/{productId}")
    public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request) {
	try {
	    Customer customer = getAuthenticatedCustomer(request);
	    cartService.removeProduct(productId, customer);
	    return "The product has been removed from your shopping cart.";
	} catch (CustomerNotFoundException e) {
	    return "You must login to remove product";
	}

    }

}