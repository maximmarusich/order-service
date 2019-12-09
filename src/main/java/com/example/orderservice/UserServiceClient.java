package com.example.orderservice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;
    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserDTO getUser(long userId) {
        return restTemplate.getForObject(userServiceUrl + "/api/v1/users/" + userId, UserDTO.class);
    }
}
