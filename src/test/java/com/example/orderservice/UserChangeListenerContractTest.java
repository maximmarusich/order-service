package com.example.orderservice;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.model.v3.messaging.Message;
import au.com.dius.pact.model.v3.messaging.MessagePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {OrderServiceApplication.class})
@PactTestFor(providerName = "user-service-messaging", port = "8088", providerType = ProviderType.ASYNCH)
@Slf4j
public class UserChangeListenerContractTest {

    private final long USER_ID = 10;
    @Autowired
    private ObjectMapper objectMapper;


    @Pact(consumer = "order-service-messaging", provider = "user-service-messaging")
    public MessagePact userChangedMessagePact(MessagePactBuilder builder) {
        return builder
                .given("Receive user data upon change event")
                .expectsToReceive("user data change message")
                .withContent(userResponseBody())
                .toPact();
    }

    private DslPart userResponseBody() {
        return new PactDslJsonBody()
                .numberType("id", USER_ID)
                .stringType("email", "some@gmail.com");

    }

    @Test
    @PactTestFor(pactMethod = "userChangedMessagePact")
    public void readUserById(List<Message> messages) {
        messages.forEach(message -> {
            try {
                UserDTO userDTO = objectMapper.readValue(message.getContents().valueAsString(), UserDTO.class);
                assertThat(userDTO.getId(), is(USER_ID));
                assertThat(userDTO.getEmail(), is("some@gmail.com"));

            } catch (Exception e) {
                log.error("Can't deserialize message", e);
                Assert.fail("Can't deserialize message : " + message.getContents().valueAsString());
            }

        });
    }
}
