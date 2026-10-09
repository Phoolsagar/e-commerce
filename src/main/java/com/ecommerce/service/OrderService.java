package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final UserService userService;

	public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
			ProductRepository productRepository, UserService userService) {
		this.orderRepository = orderRepository;
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
		this.userService = userService;
	}

	@Transactional
	public Order placeOrder(User user, String shippingAddress) {
		List<CartItem> cartItems = cartItemRepository.findByUser(user);
		if (cartItems.isEmpty()) {
			throw new IllegalArgumentException("Your cart is empty.");
		}

		Order order = new Order();
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		order.setShippingAddress(shippingAddress);
		order.setStatus("PENDING");

		double total = 0.0;
		for (CartItem item : cartItems) {
			total += item.getProduct().getPrice() * item.getQuantity();

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setPrice(item.getProduct().getPrice());
			order.getItems().add(orderItem);
		}

		order.setTotalAmount(total);
		Order savedOrder = orderRepository.save(order);
		cartItemRepository.deleteByUser(user);

		return savedOrder;
	}

	public List<Order> getOrdersForUser(User user) {
		return orderRepository.findByUserOrderByOrderDateDesc(user);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAllByOrderByOrderDateDesc();
	}

	public List<Order> getOrdersForAdmin(User admin) {
		return orderRepository.findDistinctByItemsProductCreatedByOrderByOrderDateDesc(admin);
	}

	public Order updateOrderStatus(Integer orderId, String status) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Order not found"));

		order.setStatus(status);
		return orderRepository.save(order);
	}

	@Transactional
	public Order createOrderForAdmin(User admin, Integer userId, Integer productId, int quantity,
			String shippingAddress, String status) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be at least 1");
		}

		User customer = userService.getUserById(userId);
		var product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		assertProductOwnership(product, admin);

		Order order = new Order();
		order.setUser(customer);
		order.setOrderDate(LocalDateTime.now());
		order.setShippingAddress(shippingAddress);
		order.setStatus(status == null || status.isBlank() ? "PENDING" : status);

		OrderItem item = new OrderItem();
		item.setOrder(order);
		item.setProduct(product);
		item.setQuantity(quantity);
		item.setPrice(product.getPrice());
		order.getItems().add(item);
		order.setTotalAmount(product.getPrice() * quantity);

		return orderRepository.save(order);
	}

	public void updateOrderStatusForAdmin(Integer orderId, String status, User admin) {
		Order order = getOrderForAdmin(orderId, admin);
		order.setStatus(status);
		orderRepository.save(order);
	}

	public void deleteOrderForAdmin(Integer orderId, User admin) {
		orderRepository.delete(getOrderForAdmin(orderId, admin));
	}

	private Order getOrderForAdmin(Integer orderId, User admin) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Order not found"));
		if (order.getItems().stream().anyMatch(item -> item.getProduct().getCreatedBy() == null
				|| !item.getProduct().getCreatedBy().getId().equals(admin.getId()))) {
			throw new IllegalArgumentException("You can only manage orders containing your products");
		}
		return order;
	}

	private void assertProductOwnership(com.ecommerce.model.Product product, User admin) {
		if (product.getCreatedBy() == null || !product.getCreatedBy().getId().equals(admin.getId())) {
			throw new IllegalArgumentException("You can only manage orders for your products");
		}
	}
}
