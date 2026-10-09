package com.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;

@Service
public class CartService {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
	}

	public List<CartItem> getCartItems(User user) {
		return cartItemRepository.findByUser(user);
	}

	public double getCartTotal(User user) {
		return cartItemRepository.findByUser(user).stream()
				.mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
				.sum();
	}

	public void addToCart(User user, Integer productId, int quantity) {
		if (quantity <= 0) {
			return;
		}

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));

		Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);
		if (existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartItemRepository.save(cartItem);
		} else {
			CartItem cartItem = new CartItem();
			cartItem.setUser(user);
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItemRepository.save(cartItem);
		}
	}

	public void updateQuantity(User user, Integer productId, int quantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
				.orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

		if (quantity <= 0) {
			cartItemRepository.delete(cartItem);
			return;
		}

		cartItem.setQuantity(quantity);
		cartItemRepository.save(cartItem);
	}

	public void removeFromCart(User user, Integer productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		cartItemRepository.findByUserAndProduct(user, product)
				.ifPresent(cartItemRepository::delete);
	}

	public void clearCart(User user) {
		cartItemRepository.deleteByUser(user);
	}
}
