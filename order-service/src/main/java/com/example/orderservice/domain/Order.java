package com.example.orderservice.domain;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false, length = 120)
    private String userId;
    @Column(nullable = false, length = 120, unique = true)
    private String orderId;
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private Integer totalPrice;
    @Column(nullable = false)
    private Integer unitPrice;


    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

}
