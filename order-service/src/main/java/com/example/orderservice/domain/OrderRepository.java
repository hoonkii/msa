package com.example.orderservice.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>
{
    Order findById(String id);
    Iterable<Order> findByUserId(String userId);
}
