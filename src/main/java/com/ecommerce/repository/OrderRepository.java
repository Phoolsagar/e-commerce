package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findAllByOrderByOrderDateDesc();
    List<Order> findDistinctByItemsProductCreatedByOrderByOrderDateDesc(User createdBy);
}
