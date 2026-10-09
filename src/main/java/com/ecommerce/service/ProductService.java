package com.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public List<Product> searchProducts(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return productRepository.findAll();
		}
		return productRepository.findByNameContainingIgnoreCase(keyword.trim());
	}

	public List<Product> getProductsByCategory(Integer categoryId) {
		if (categoryId == null) {
			return productRepository.findAll();
		}
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found"));
		return productRepository.findByCategory(category);
	}

	public Page<Product> getPublicProducts(String keyword, Integer categoryId, int page, int size) {
		PageRequest request = PageRequest.of(Math.max(page, 0), size,
				Sort.by(Sort.Direction.DESC, "id"));
		if (keyword != null && !keyword.isBlank()) {
			return productRepository.findByNameContainingIgnoreCase(keyword.trim(), request);
		}
		if (categoryId != null) {
			Category category = categoryRepository.findById(categoryId)
					.orElseThrow(() -> new IllegalArgumentException("Category not found"));
			return productRepository.findByCategory(category, request);
		}
		return productRepository.findAll(request);
	}

	public Product getProductById(Integer id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
	}

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	public Page<Product> getProductsForAdmin(User admin, int page, int size) {
		return productRepository.findByCreatedByOrderByIdDesc(admin,
				PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
	}

	public List<Product> getProductsForAdmin(User admin) {
		return productRepository.findByCreatedBy(admin);
	}

	public Product saveProductForAdmin(Product product, User admin) {
		product.setCreatedBy(admin);
		return productRepository.save(product);
	}

	public Product getProductForAdmin(Integer id, User admin) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		if (product.getCreatedBy() == null || !product.getCreatedBy().getId().equals(admin.getId())) {
			throw new IllegalArgumentException("You can only edit products created by your account");
		}
		return product;
	}

	public Product updateProductForAdmin(Product changes, User admin) {
		Product product = getProductForAdmin(changes.getId(), admin);
		product.setName(changes.getName());
		product.setDescription(changes.getDescription());
		product.setPrice(changes.getPrice());
		product.setStock(changes.getStock());
		product.setImageUrl(changes.getImageUrl());
		product.setCategory(changes.getCategory());
		return productRepository.save(product);
	}

	public void deleteProduct(Integer id, User admin) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		if (product.getCreatedBy() == null || !product.getCreatedBy().getId().equals(admin.getId())) {
			throw new IllegalArgumentException("You can only manage products created by your account");
		}
		productRepository.delete(product);
	}
}
