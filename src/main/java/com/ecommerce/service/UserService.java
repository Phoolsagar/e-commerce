package com.ecommerce.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		return user;
	}

	public User registerUser(User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new IllegalArgumentException("User already exists with this email");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (user.getRole() == null || user.getRole().isBlank()) {
			user.setRole("USER");
		}
		return userRepository.save(user);
	}

	public User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
			return null;
		}

		return userRepository.findByEmail(authentication.getName())
				.orElse(null);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Integer id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}
