package com.example.orderservice;

import lombok.Data;

@Data
public class OrderDTO {

    private long id;
    private String name;
    private long userId;
}
