package com.ecommerce.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;

@Controller
public class ProductController {

	private final ProductService productService;
	private final CategoryRepository categoryRepository;
	private final UserService userService;

	public ProductController(ProductService productService, CategoryRepository categoryRepository,
			UserService userService) {
		this.productService = productService;
		this.categoryRepository = categoryRepository;
		this.userService = userService;
	}

	@GetMapping("/products")
	public String listProducts(@RequestParam(required = false) String keyword,
							  @RequestParam(required = false) Integer categoryId,
							  @RequestParam(defaultValue = "0") int page,
							  Model model) {
		Page<Product> products = productService.getPublicProducts(keyword, categoryId, page, 12);

		model.addAttribute("products", products);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("selectedCategoryId", categoryId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", products.getNumber());
		model.addAttribute("totalPages", products.getTotalPages());
		return "products";
	}

	@GetMapping("/product/{id}")
	public String showProduct(@PathVariable Integer id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		return "product-detail";
	}

	@GetMapping("/admin/products")
	public String adminProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) Integer editId, Model model) {
		if (userService.getLoggedInUser() == null) {
			return "redirect:/login";
		}
		Page<Product> products = productService.getProductsForAdmin(userService.getLoggedInUser(), Math.max(page, 0), 24);
		model.addAttribute("products", products);
		model.addAttribute("categories", categoryRepository.findByCreatedBy(userService.getLoggedInUser()));
		model.addAttribute("product", editId == null
				? new Product()
				: productService.getProductForAdmin(editId, userService.getLoggedInUser()));
		return "admin-products";
	}

	@PostMapping("/admin/products/save")
	public String saveProduct(@RequestParam Integer categoryId,
							  @RequestParam String name,
							  @RequestParam String description,
							  @RequestParam double price,
							  @RequestParam int stock,
							  @RequestParam String imageUrl) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found"));
		if (category.getCreatedBy() == null || !category.getCreatedBy().getId().equals(userService.getLoggedInUser().getId())) {
			throw new IllegalArgumentException("You can only use categories created by your account");
		}

		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setStock(stock);
		product.setImageUrl(imageUrl);
		product.setCategory(category);
		productService.saveProductForAdmin(product, userService.getLoggedInUser());
		return "redirect:/admin/products";
	}

	@PostMapping("/admin/products/update")
	public String updateProduct(@RequestParam Integer id,
			@RequestParam Integer categoryId,
			@RequestParam String name,
			@RequestParam String description,
			@RequestParam double price,
			@RequestParam int stock,
			@RequestParam String imageUrl) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found"));
		if (category.getCreatedBy() == null || !category.getCreatedBy().getId().equals(userService.getLoggedInUser().getId())) {
			throw new IllegalArgumentException("You can only use categories created by your account");
		}

		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setStock(stock);
		product.setImageUrl(imageUrl);
		product.setCategory(category);
		productService.updateProductForAdmin(product, userService.getLoggedInUser());
		return "redirect:/admin/products";
	}

	@PostMapping("/admin/products/delete/{id}")
	public String deleteProduct(@PathVariable Integer id) {
		productService.deleteProduct(id, userService.getLoggedInUser());
		return "redirect:/admin/products";
	}
}
