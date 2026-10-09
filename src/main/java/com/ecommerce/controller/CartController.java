package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService;
	private final UserService userService;
	private final OrderService orderService;

	public CartController(CartService cartService, UserService userService, OrderService orderService) {
		this.cartService = cartService;
		this.userService = userService;
		this.orderService = orderService;
	}

	@GetMapping
	public String cart(Model model) {
		User user = userService.getLoggedInUser();
		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("cartItems", cartService.getCartItems(user));
		model.addAttribute("total", cartService.getCartTotal(user));
		return "cart";
	}

	@PostMapping("/add/{productId}")
	public String addToCart(@PathVariable Integer productId,
							@RequestParam(defaultValue = "1") int quantity,
							RedirectAttributes redirectAttributes) {
		User user = userService.getLoggedInUser();
		if (user == null) {
			return "redirect:/login";
		}

		cartService.addToCart(user, productId, quantity);
		redirectAttributes.addFlashAttribute("successMessage", "Product added to cart");
		return "redirect:/products";
	}

	@PostMapping("/update/{productId}")
	public String updateQuantity(@PathVariable Integer productId,
								@RequestParam int quantity) {
		User user = userService.getLoggedInUser();
		if (user == null) {
			return "redirect:/login";
		}

		cartService.updateQuantity(user, productId, quantity);
		return "redirect:/cart";
	}

	@PostMapping("/remove/{productId}")
	public String removeFromCart(@PathVariable Integer productId) {
		User user = userService.getLoggedInUser();
		if (user == null) {
			return "redirect:/login";
		}

		cartService.removeFromCart(user, productId);
		return "redirect:/cart";
	}

	@PostMapping("/checkout")
	public String checkout(@RequestParam String shippingAddress, RedirectAttributes redirectAttributes) {
		User user = userService.getLoggedInUser();
		if (user == null) {
			return "redirect:/login";
		}

		try {
			orderService.placeOrder(user, shippingAddress);
			redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully");
			return "redirect:/orders";
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/cart";
		}
	}
}
