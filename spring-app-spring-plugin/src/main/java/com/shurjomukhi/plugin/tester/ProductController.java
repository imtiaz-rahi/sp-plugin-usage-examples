package com.shurjomukhi.plugin.tester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shurjopay.plugin.model.PaymentRes;
/**
 * Root controller to manage product payment cycle
 * 
 * @author Al - Amin
 * @since 2022-07-18
 */
@Controller
@RequestMapping("/")
public class ProductController {

	@Autowired
	private ProductService service;

	/**
	 * 
	 * @param model
	 * @return root page
	 */
	@GetMapping
	public String showProductPage(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		return "product";
	}

	@PostMapping("/buy-product")
	public String buyProduct(@ModelAttribute("product") Product product, Model model) {
		product.setProductName("Pen");
		product.setProductPrice(1.0);
		PaymentRes response = service.buy(product);
		return "redirect:" + response.getPaymentUrl();
	}

	/**
	 * Get shurjopay response to get order id and verify payment here
	 * @param orderId
	 * @param model
	 * @return
	 */
	@GetMapping("/response")
	public String paymentResponse(@RequestParam("order_id") String orderId, Model model) {
		if (service.verifyOrder(orderId)) {
			model.addAttribute("msg", "Congrats! Your payment has been successfully submitted");
			model.addAttribute("orderId", orderId);
		} else
			model.addAttribute("msg", "Opps! Payment submission failed");
		return "status";
	}

	/**
	 * Checking payment status by order id
	 * @param order id
	 * @param model
	 * @return payment status page to show the current state of payment
	 */
	@GetMapping("/payment-status/{id}")
	public String paymentStutus(@PathVariable("id") String id, Model model) {
		model.addAttribute("paymentStat", service.checkPaymentStatus(id));
		System.out.println(service.checkPaymentStatus(id));
		return "payment-status";
	}

}