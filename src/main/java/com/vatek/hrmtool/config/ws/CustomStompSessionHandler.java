package com.vatek.hrmtool.config.ws;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

@Log4j2
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("Connected to STOMP broker: {}", connectedHeaders);
        // Subscribe to a destination upon connection
        session.subscribe("/queue/driver-locations", this);
        log.info("Subscribed to /queue/driver-locations");
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("Received message with header : {} and payload : {}", headers , payload);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        log.info("getPayloadType : {}",headers);
        return super.getPayloadType(headers);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("handleException : ", exception);
        super.handleException(session, command, headers, payload, exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Transport error: {}", exception.getMessage(), exception);
    }
}
