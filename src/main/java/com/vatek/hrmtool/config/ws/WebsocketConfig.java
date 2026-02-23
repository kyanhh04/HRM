package com.vatek.hrmtool.config.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;

@Configuration
public class WebsocketConfig {
    @Bean
    public WebSocketStompClient webSocketStompClient() {
        // Initialize the WebSocket client
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        List<MessageConverter> converters = List.of(
                new MappingJackson2MessageConverter(),
                new StringMessageConverter(),
                new SimpleMessageConverter()
        );

        CompositeMessageConverter compositeMessageConverter = new CompositeMessageConverter(converters);

        // Set the message converter (e.g., JSON)
        stompClient.setMessageConverter(compositeMessageConverter);

        return stompClient;
    }

    @Bean
    public StompSessionHandler stompSessionHandler() {
        // Implement your custom session handler
        return new CustomStompSessionHandler();
    }
}
