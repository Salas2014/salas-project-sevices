package com.salas.email.consumer.handler;

import com.salas.common.events.DepositRequestEvent;
import com.salas.common.events.WithdrawRequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {"deposit-event-topic", "withdraw-event-topic"})
public class TransferEventHandler {

    public final Logger log = LoggerFactory.getLogger(getClass());

    @KafkaHandler
    public void handleWithdraw(@Payload WithdrawRequestEvent event) {
        log.info("Create withdraw request event received amount: {}", event.getAmount());
    }

    @KafkaHandler
    public void handleDeposit(@Payload DepositRequestEvent event) {
        log.info("Create deposit request event received amount: {}", event.getAmount());
    }
}
