package com.ecommerce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;

@SpringBootApplication
public class ECommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               CategoryRepository categoryRepository,
                               ProductRepository productRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            List<User> admins = new ArrayList<>();
            for (int adminNumber = 1; adminNumber <= 5; adminNumber++) {
                String email = adminNumber == 1
                        ? "admin@ecommerce.com"
                        : "admin" + adminNumber + "@ecommerce.com";
                admins.add(ensureAdmin(userRepository, passwordEncoder, email,
                        "Admin " + adminNumber, "admin123"));
            }
            User primaryAdmin = admins.get(0);

            if (categoryRepository.count() == 0) {
                Category electronics = new Category();
                electronics.setName("Electronics");
                electronics.setDescription("Smart devices and gadgets");
                categoryRepository.save(electronics);

                Category fashion = new Category();
                fashion.setName("Fashion");
                fashion.setDescription("Clothing and accessories");
                categoryRepository.save(fashion);

                Category home = new Category();
                home.setName("Home");
                home.setDescription("Everything for your home");
                categoryRepository.save(home);
            }

            List<Category> legacyCategories = categoryRepository.findByCreatedByIsNull();
            if (!legacyCategories.isEmpty()) {
                for (Category category : legacyCategories) {
                    category.setCreatedBy(primaryAdmin);
                }
                categoryRepository.saveAll(legacyCategories);
            }

            if (productRepository.count() == 0) {
                Category electronics = categoryRepository.findAll().get(0);
                Category fashion = categoryRepository.findAll().get(1);
                Category home = categoryRepository.findAll().get(2);

                productRepository.saveAll(List.of(
                    new Product(null, "Wireless Headphones", "Noise-cancelling headphones with 30-hour battery life.", 299.99, 25, "https://images.unsplash.com/photo-1546435770-a3e426bf472b?auto=format&fit=crop&w=900&q=80", electronics, primaryAdmin),
                    new Product(null, "Smart Watch", "Track your health and notifications from your wrist.", 199.99, 35, "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=80", electronics, primaryAdmin),
                    new Product(null, "Cotton Hoodie", "Classic hoodie made from soft cotton fabric.", 59.99, 50, "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?auto=format&fit=crop&w=900&q=80", fashion, primaryAdmin),
                    new Product(null, "Desk Lamp", "Minimal lamp with warm LED light and touch control.", 39.99, 18, "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=900&q=80", home, primaryAdmin)
                ));
            }

            List<Product> legacyProducts = productRepository.findByCreatedByIsNull();
            if (!legacyProducts.isEmpty()) {
                for (Product product : legacyProducts) {
                    product.setCreatedBy(primaryAdmin);
                }
                productRepository.saveAll(legacyProducts);
            }

            String[] productImages = {
                    "https://images.unsplash.com/photo-1546435770-a3e426bf472b?auto=format&fit=crop&w=900&q=80",
                    "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=80",
                    "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?auto=format&fit=crop&w=900&q=80",
                    "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=900&q=80",
                    "https://images.unsplash.com/photo-1494438639946-1ebd1d20bf85?auto=format&fit=crop&w=900&q=80"
            };

            for (int adminIndex = 0; adminIndex < admins.size(); adminIndex++) {
                User admin = admins.get(adminIndex);
                List<Category> adminCategories = List.of(
                    ensureCategory(categoryRepository, admin, "Admin " + (adminIndex + 1) + " Tech", "Technology and useful gadgets."),
                    ensureCategory(categoryRepository, admin, "Admin " + (adminIndex + 1) + " Home", "Objects for a more considered home."),
                    ensureCategory(categoryRepository, admin, "Admin " + (adminIndex + 1) + " Style", "Everyday fashion and personal style."),
                    ensureCategory(categoryRepository, admin, "Admin " + (adminIndex + 1) + " Lifestyle", "Small upgrades for everyday life."));
                List<Product> ownedProducts = productRepository.findByCreatedBy(admin);
                int productNumber = ownedProducts.size() + 1;

                while (ownedProducts.size() < 20) {
                    String productName = "Admin " + (adminIndex + 1) + " Product "
                            + String.format("%02d", productNumber);
                    if (productRepository.findByName(productName).isEmpty()) {
                        Product product = new Product();
                        product.setName(productName);
                        product.setDescription("A curated catalog item managed by Admin " + (adminIndex + 1) + ".");
                        product.setPrice(49.99 + (adminIndex * 25) + productNumber);
                        product.setStock(8 + (productNumber % 18));
                        product.setImageUrl(productImages[(productNumber - 1) % productImages.length]);
                        product.setCategory(adminCategories.get((productNumber - 1) % adminCategories.size()));
                        product.setCreatedBy(admin);
                        productRepository.save(product);
                        ownedProducts.add(product);
                    }
                    productNumber++;
                }

                for (int productIndex = 0; productIndex < ownedProducts.size(); productIndex++) {
                    ownedProducts.get(productIndex).setCategory(
                            adminCategories.get(productIndex % adminCategories.size()));
                }
                productRepository.saveAll(ownedProducts);
            }
        };
    }

    private User ensureAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder,
                             String email, String displayName, String password) {
        User admin = userRepository.findByEmail(email).orElse(null);
        if (admin == null) {
            admin = new User();
            admin.setEmail(email);
        }
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFirstName(displayName);
        admin.setLastName("Admin");
        admin.setRole("ADMIN");
        return userRepository.save(admin);
    }

    private Category ensureCategory(CategoryRepository categoryRepository, User admin,
                                    String name, String description) {
        Category category = categoryRepository.findByName(name).orElseGet(Category::new);
        category.setName(name);
        category.setDescription(description);
        category.setCreatedBy(admin);
        return categoryRepository.save(category);
    }
}
