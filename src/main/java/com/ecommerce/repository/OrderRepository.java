package com.ecommerce.repository;

import com.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findById(long id);
    List<Orders> getOrdersByEmail(String email);
}
