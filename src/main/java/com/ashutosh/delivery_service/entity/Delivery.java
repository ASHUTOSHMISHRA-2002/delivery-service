package com.ashutosh.delivery_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @Column(nullable = false)
    private Long agentId;

    @Column(nullable = false)
    private Long orderId;  // ✅ Foreign key to order-service (stored as Long)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(nullable = false)
    private LocalDateTime estimatedTimeOfArrival;

    public Delivery() {
        // Default constructor
    }

    public Delivery(Long deliveryId, Long agentId, Long orderId, DeliveryStatus status, LocalDateTime estimatedTimeOfArrival) {
        this.deliveryId = deliveryId;
        this.agentId = agentId;
        this.orderId = orderId;
        this.status = status;
        this.estimatedTimeOfArrival = estimatedTimeOfArrival;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public LocalDateTime getEstimatedTimeOfArrival() {
        return estimatedTimeOfArrival;
    }

    public void setEstimatedTimeOfArrival(LocalDateTime estimatedTimeOfArrival) {
        this.estimatedTimeOfArrival = estimatedTimeOfArrival;
    }
}
