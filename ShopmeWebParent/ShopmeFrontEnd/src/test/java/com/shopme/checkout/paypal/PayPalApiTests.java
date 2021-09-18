package com.shopme.checkout.paypal;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {

    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "AXCeCbEX9xFPLycRqFboe4MLFAhfeU2HWCMKf9noggpA6vo0FLaiIR6nXT9uvCsX8O14w3KstIWipF0H";
    private static final String CLIENT_SECRET = "EOfsnXumX41f46__sQcxLv_lJHGYJEfISiRHOYSFca9qrEYRvqdfihW1cP8UZ-dnRbGNwcyC3b8a3a5a";

    @Test
    public void testGetOrderDetails() {
	String orderId = "08829460TU591062T";
	String requestURL = BASE_URL + GET_ORDER_API + orderId;

	HttpHeaders headers = new HttpHeaders();
	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	headers.add("Accept-Language", "en_US");
	headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
	HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
	RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
	PayPalOrderResponse orderResponse = response.getBody();
	System.out.println(orderResponse.getId() + " " +orderResponse.getStatus());
	
	System.out.println(orderResponse.validate(orderId));
    }
}
