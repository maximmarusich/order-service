package com.example.orderservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final UserServiceClient userServiceClient;

    @PostMapping
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        UserDTO user = userServiceClient.getUser(orderDTO.getUserId());
        orderDTO.setId(30L);
        log.info("Sending order creation notification for user : {}", user);
        return orderDTO;
    }





}
