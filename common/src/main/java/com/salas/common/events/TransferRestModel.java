package com.salas.common.events;

import java.math.BigDecimal;

public class TransferRestModel {

    private String senderId;
    private String receiverId;
    private BigDecimal amount;

    public TransferRestModel() {
    }

    public TransferRestModel(String senderId, String receiverId, BigDecimal amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
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
