package com.ecommerce.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(Category category);
    List<Product> findByNameContainingIgnoreCase(String keyword);
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
    java.util.Optional<Product> findByName(String name);
    Page<Product> findByCreatedByOrderByIdDesc(User createdBy, Pageable pageable);
    List<Product> findByCreatedBy(User createdBy);
    List<Product> findByCreatedByIsNull();
}
