package com.example.orderservice.controller;

import com.example.orderservice.domain.Order;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.OrderRequest;
import com.example.orderservice.vo.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status(){
        return String.format("Its working in catalog service %s",env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable("userId")String userId, @RequestBody OrderRequest orderRequest){
        log.info("Before call order create");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);
        OrderDto orderDto = mapper.map(orderRequest, OrderDto.class);
        orderDto.setUserId(userId);
        orderDto.setOrderId(UUID.randomUUID().toString());

        orderDto.setTotalPrice(orderRequest.getQty() * orderRequest.getUnitPrice());
        /* 오더 정보를 카프카에게 전달 */
//        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);
        orderService.createOrder(orderDto);
        OrderResponse orderResponse = mapper.map(orderDto, OrderResponse.class);
        log.info("After call order create");

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> findUserOrders(@PathVariable("userId")String userId){
        log.info("Call from user service");
        Iterable<Order> orderList = orderService.getOrdersByUserId(userId);
        List<OrderResponse> result = new ArrayList<>();

        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, OrderResponse.class));
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}
