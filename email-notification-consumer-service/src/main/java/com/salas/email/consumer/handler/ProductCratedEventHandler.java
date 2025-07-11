package com.salas.email.consumer.handler;

import com.salas.common.events.CreateProductEvent;
import com.salas.email.consumer.exceptions.NonRetryableException;
import com.salas.email.consumer.exceptions.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "product-created-event-topic")
public class ProductCratedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;

    @Autowired
    public ProductCratedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handle(CreateProductEvent event) {
        LOGGER.info("Create product event received: {}", event.getTitle());

        var url = "http://localhost:8090";

        try {
            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (exchange.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Received product event: {}", exchange.getBody());
            }
        } catch (HttpServerErrorException e) {
            LOGGER.error(e.getMessage());
            throw new NonRetryableException(e.getMessage());
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
            throw new RetryableException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NonRetryableException(e.getMessage());
        }
    }
}
