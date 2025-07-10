package com.salas.productservice.service;

import com.salas.common.events.CreateProductEvent;
import com.salas.productservice.dto.CreatedProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

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
        SendResult<String, Object> result;
        try {
            result = kafkaTemplate.send(topicName, productId, createProductEvent).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }


        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        return productId;
    }
}
