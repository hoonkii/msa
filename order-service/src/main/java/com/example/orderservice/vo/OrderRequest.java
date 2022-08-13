package com.example.orderservice.vo;

import lombok.Data;

@Data
public class OrderRequest {
    private String productId;
    private Integer quantity;
    private Integer unitPrice;

}
