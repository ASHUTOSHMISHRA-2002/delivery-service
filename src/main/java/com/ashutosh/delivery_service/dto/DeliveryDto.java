package com.ashutosh.delivery_service.dto;

import com.ashutosh.delivery_service.entity.DeliveryStatus;

import java.time.LocalDateTime;

public class DeliveryDto {

    private Long agentId;
    private Long orderId; // ✅ Received in request, used in DB
    private DeliveryStatus status;
    private LocalDateTime estimatedTimeOfArrival;
    private OrderDto order; // ✅ Enriched in response (optional)

    // Default constructor
    public DeliveryDto() {
    }

    // All-args constructor
    public DeliveryDto(Long agentId, Long orderId, DeliveryStatus status,
                       LocalDateTime estimatedTimeOfArrival, OrderDto order) {
        this.agentId = agentId;
        this.orderId = orderId;
        this.status = status;
        this.estimatedTimeOfArrival = estimatedTimeOfArrival;
        this.order = order;
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

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "DeliveryDto{" +
                "agentId=" + agentId +
                ", orderId=" + orderId +
                ", status=" + status +
                ", estimatedTimeOfArrival=" + estimatedTimeOfArrival +
                ", order=" + order +
                '}';
    }
}
