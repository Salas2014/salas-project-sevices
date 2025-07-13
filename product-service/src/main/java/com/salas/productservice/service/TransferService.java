package com.salas.productservice.service;

import com.salas.common.events.CustomEvent;
import com.salas.common.events.DepositRequestEvent;
import com.salas.common.events.TransferRestModel;
import com.salas.common.events.WithdrawRequestEvent;
import com.salas.productservice.exceptions.TransferServiceException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@Service
public class TransferService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestTemplate restTemplate;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Environment environment;

    @Autowired
    public TransferService(KafkaTemplate<String, Object> kafkaTemplate, RestTemplate restTemplate, Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    @Transactional
    public boolean transfer(TransferRestModel model) {
        var withDrowEvent = new WithdrawRequestEvent(model.getSenderId(), model.getReceiverId(), model.getAmount());
        var depositEvent = new DepositRequestEvent(model.getSenderId(), model.getReceiverId(), model.getAmount());

        try {
            String topicWithdrawName = Objects.requireNonNull(environment.getProperty("topics.topics-name.topic-withdraw"));
            var withdrawRecord = buildRecord(topicWithdrawName, withDrowEvent);
            SendResult<String, Object> result = kafkaTemplate.send(withdrawRecord).get();
            log.info("Sent event to withdraw topic with partition: {} and offset: {}",
                    result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

            callRemoteService();

            String depositTopicName = Objects.requireNonNull(environment.getProperty("topics.topics-name.topic-deposit"));
            var depositRecord = buildRecord(depositTopicName, depositEvent);
            SendResult<String, Object> depositResult = kafkaTemplate.send(depositRecord).get();

            log.info("Sent event to deposit topic with partition: {} and offset: {}",
                    depositResult.getRecordMetadata().partition(), depositResult.getRecordMetadata().offset());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TransferServiceException(e);
        }

        return true;
    }

    private void callRemoteService() {
        String fullUrl = "http://localhost:8090/response/200";

        ResponseEntity<String> exchange = restTemplate.exchange(fullUrl, HttpMethod.GET, null, String.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            log.info("Remote service call done with status code %s".formatted(exchange.getStatusCode()));
        }
    }

    private static ProducerRecord<String, Object> buildRecord(String topicName, CustomEvent event) {
        String eventKey = UUID.randomUUID().toString();
        return new ProducerRecord<>(topicName, eventKey, event);
    }

}
