package com.salas.email.consumer.handler;

import com.salas.common.events.CreateProductEvent;
import com.salas.email.consumer.exceptions.NonRetryableException;
import com.salas.email.consumer.exceptions.RetryableException;
import com.salas.email.consumer.persistence.entity.ProcessedEventEntity;
import com.salas.email.consumer.persistence.repo.ProcessedEventEntityRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@KafkaListener(topics = "salas-odesa-topic")
public class ProductCratedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;
    private final ProcessedEventEntityRepo repo;

    @Autowired
    public ProductCratedEventHandler(RestTemplate restTemplate, ProcessedEventEntityRepo processedEventEntityRepo) {
        this.restTemplate = restTemplate;
        this.repo = processedEventEntityRepo;
    }

    @KafkaHandler
    public void handle(
            @Payload CreateProductEvent event,
            @Header("messageId") String messageId) {
        LOGGER.info("Create product event received: {}", event.getTitle());

        Optional<ProcessedEventEntity> byMessageId = repo.findByMessageId(messageId);

        if (byMessageId.isPresent()) {
            LOGGER.info("Duplicate message id : {}", byMessageId.get().getMessageId());
            return;
        }

//        callRemote();

        try{
            repo.save(new ProcessedEventEntity(messageId, event.getProductId()));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error(e.getMessage());
            throw new NonRetryableException(e.getMessage());
        }
    }

    private void callRemote() {
        var url = "http://localhost:8090/response/200";

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
