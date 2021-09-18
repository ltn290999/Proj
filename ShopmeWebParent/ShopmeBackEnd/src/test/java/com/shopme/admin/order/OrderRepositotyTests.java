package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositotyTests {

    @Autowired
    private OrderRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewOrderWithSingleProduct() {
	Customer customer = entityManager.find(Customer.class, 1);
	Product product = entityManager.find(Product.class, 1);
	
	Order mainOrder = new Order();
	mainOrder.setOrderTime(new Date());
	mainOrder.setCustomer(customer);
	mainOrder.copyAddressFromCustomer();
	mainOrder.setShippingCost(10);
	mainOrder.setProductCost(product.getCost());
	mainOrder.setTax(0);
	mainOrder.setSubtotal(product.getPrice());
	mainOrder.setTotal(product.getPrice() + 10);
	mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
	mainOrder.setStatus(OrderStatus.NEW);
	mainOrder.setDeliverDate(new Date());
	mainOrder.setDeliverDays(1);
	
	OrderDetail  orderDetail = new OrderDetail();
	orderDetail.setProduct(product);
	orderDetail.setOrder(mainOrder);
	orderDetail.setProductCost(product.getCost());
	orderDetail.setQuantity(1);
	orderDetail.setUnitPrice(product.getPrice());
	orderDetail.setSubtotal(1);
	orderDetail.setShippingCost(product.getPrice());
	
	mainOrder.getOrderDetails().add(orderDetail);
	Order savedOrder = repo.save(mainOrder);
	assertThat(savedOrder.getId()).isGreaterThan(0);
	
	
    }
}
