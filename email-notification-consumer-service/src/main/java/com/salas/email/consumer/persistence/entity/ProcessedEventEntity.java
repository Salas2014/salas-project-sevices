package com.salas.email.consumer.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "processed_events")
public class ProcessedEventEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,  unique = true)
    private String messageId;

    @Column(nullable = false)
    private String productid;

    public ProcessedEventEntity(String messageid, String productid) {
        this.messageId = messageid;
        this.productid = productid;
    }

    public ProcessedEventEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }
}
