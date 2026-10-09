package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.model.User;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;

@Controller
public class OrderController {

	private final OrderService orderService;
	private final UserService userService;

	public OrderController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@GetMapping("/orders")
	public String orders(Model model) {
		User user = userService.getLoggedInUser();
		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("orders", orderService.getOrdersForUser(user));
		return "orders";
	}
}
