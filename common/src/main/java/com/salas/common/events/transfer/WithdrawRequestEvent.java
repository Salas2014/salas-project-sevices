package com.salas.common.events.transfer;

import java.math.BigDecimal;

public class WithdrawRequestEvent implements CustomEvent {

    private String senderId;
    private String receiverId;
    private BigDecimal amount;

    public WithdrawRequestEvent(String senderId, String receiverId, BigDecimal amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public WithdrawRequestEvent() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
