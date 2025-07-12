package com.salas.productservice.service;

import com.salas.common.events.CreateProductEvent;
import com.salas.productservice.dto.CreatedProductDto;
import org.apache.kafka.clients.producer.ProducerRecord;
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
    @Value("${topics.topics-name.topic-product:}")
    private String topicName;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public ProductService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String createProduct(CreatedProductDto product) throws ExecutionException, InterruptedException {
        String productId = UUID.randomUUID().toString();
        ProducerRecord<String, Object> record = buildRecord(product, productId);

        SendResult<String, Object> result = kafkaTemplate.send(record).get();

        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        return productId;
    }

    private ProducerRecord<String, Object> buildRecord(CreatedProductDto product, String productId) {
        var createProductEvent = new CreateProductEvent(productId, product.getTitle(), product.getPrice(), product.getCount());
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                topicName,
                productId,
                createProductEvent);
        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());
        return record;
    }
}
