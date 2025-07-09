package com.salas.productservice.service;

import com.salas.productservice.dto.CreateProductEvent;
import com.salas.productservice.dto.CreatedProductDto;
import com.salas.productservice.dto.EventCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {
    @Value("${topics.topic.name:}")
    private String topicName;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public ProductService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String createProduct(CreatedProductDto product) {
        String productId = UUID.randomUUID().toString();
        var createProductEvent = new CreateProductEvent(productId, product.getTitle(), product.getPrice(), product.getCount());
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, productId, createProductEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send product event: {}", exception.getMessage());
            } else {
                LOGGER.info("Successfully sent product event: {}" , result.getRecordMetadata());
            }
        });

        LOGGER.info("Return: {}", product);

        return productId;
    }
}
