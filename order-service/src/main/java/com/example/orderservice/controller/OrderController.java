package com.example.orderservice.controller;

import com.example.orderservice.domain.Order;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.OrderRequest;
import com.example.orderservice.vo.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;
    private final OrderService orderService;

    @GetMapping("/health_check")
    public String status(){
        return String.format("Its working in catalog service %s",env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable("userId")String userId, @RequestBody OrderRequest orderRequest){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);

        OrderDto orderDto = mapper.map(orderRequest, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createdOrder = orderService.createOrder(orderDto);

        OrderResponse order = mapper.map(createdOrder, OrderResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> findUserOrders(@PathVariable("userId")String userId){
        Iterable<Order> orderList = orderService.getOrdersByUserId(userId);
        List<OrderResponse> result = new ArrayList<>();

        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, OrderResponse.class));
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}
