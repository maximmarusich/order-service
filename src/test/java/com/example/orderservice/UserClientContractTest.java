package com.example.orderservice;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {OrderServiceApplication.class})
@PactTestFor(providerName = "user-service", port = "8088")
public class UserClientContractTest {

    private final long USER_ID = 10;

    @Autowired
    private UserServiceClient userServiceClient;


    @Pact(consumer = "order-service", provider = "user-service")
    public RequestResponsePact readUserById(PactDslWithProvider builder) {
        return builder.given("Read User by id value")
                .uponReceiving("A request to /users/" + USER_ID)
                .path("/api/v1/users/" + USER_ID)
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(userResponseBody().asBody())
                .toPact();
    }

    private DslPart userResponseBody() {
        return new PactDslJsonBody()
                .stringType("id", String.valueOf(USER_ID))
                .stringType("email", "some@gmail.com");

    }

    @Test
    @PactTestFor(pactMethod = "readUserById")
    public void readUserById() {
        UserDTO user = userServiceClient.getUser(USER_ID);
        assertThat(user.getEmail(), is("some@gmail.com"));
    }
}
