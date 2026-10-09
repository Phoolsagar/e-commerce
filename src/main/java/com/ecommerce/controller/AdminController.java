package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final ProductService productService;
	private final OrderService orderService;
	private final CategoryRepository categoryRepository;
	private final UserService userService;

	public AdminController(ProductService productService, OrderService orderService,
			CategoryRepository categoryRepository, UserService userService) {
		this.productService = productService;
		this.orderService = orderService;
		this.categoryRepository = categoryRepository;
		this.userService = userService;
	}

	@GetMapping
	public String dashboard(Model model) {
		model.addAttribute("products", productService.getProductsForAdmin(userService.getLoggedInUser()));
		model.addAttribute("orders", orderService.getOrdersForAdmin(userService.getLoggedInUser()));
		model.addAttribute("customers", userService.getAllUsers());
		model.addAttribute("adminProducts", productService.getProductsForAdmin(userService.getLoggedInUser()));
		model.addAttribute("categories", categoryRepository.findAll());
		return "admin-dashboard";
	}

	@GetMapping("/categories")
	public String categories(Model model) {
		model.addAttribute("categories", categoryRepository.findByCreatedBy(userService.getLoggedInUser()));
		model.addAttribute("category", new Category());
		return "admin-categories";
	}

	@PostMapping("/categories/save")
	public String saveCategory(@RequestParam String name,
			@RequestParam String description) {
		Category category = new Category();
		category.setName(name.trim());
		category.setDescription(description.trim());
		category.setCreatedBy(userService.getLoggedInUser());
		categoryRepository.save(category);
		return "redirect:/admin/categories";
	}

	@PostMapping("/orders/{id}/status")
	public String updateOrderStatus(@PathVariable Integer id,
								   @RequestParam String status) {
		orderService.updateOrderStatusForAdmin(id, status, userService.getLoggedInUser());
		return "redirect:/admin";
	}

	@PostMapping("/orders/create")
	public String createOrder(@RequestParam Integer userId,
			@RequestParam Integer productId,
			@RequestParam int quantity,
			@RequestParam String shippingAddress,
			@RequestParam(defaultValue = "PENDING") String status) {
		orderService.createOrderForAdmin(userService.getLoggedInUser(), userId, productId,
				quantity, shippingAddress, status);
		return "redirect:/admin";
	}

	@PostMapping("/orders/{id}/delete")
	public String deleteOrder(@PathVariable Integer id) {
		orderService.deleteOrderForAdmin(id, userService.getLoggedInUser());
		return "redirect:/admin";
	}
}
