package com.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Category;
import com.ecommerce.model.User;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	List<Category> findByCreatedBy(User createdBy);
	List<Category> findByCreatedByIsNull();
	Optional<Category> findByName(String name);
}
