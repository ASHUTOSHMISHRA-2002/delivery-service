package com.ashutosh.delivery_service.service.impl;

import com.ashutosh.delivery_service.dto.DeliveryDto;
import com.ashutosh.delivery_service.dto.OrderDto;
import com.ashutosh.delivery_service.entity.Delivery;
import com.ashutosh.delivery_service.entity.DeliveryStatus;
import com.ashutosh.delivery_service.exception.ResourceNotFoundException;
import com.ashutosh.delivery_service.feign.OrderClient;
import com.ashutosh.delivery_service.repository.DeliveryRepository;
import com.ashutosh.delivery_service.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, OrderClient orderClient) {
        this.deliveryRepository = deliveryRepository;
        this.orderClient = orderClient;
    }

    @Override
    public ResponseEntity<DeliveryDto> assignAgent(DeliveryDto dto) {
        Long orderId = dto.getOrderId();

        // Fetch order from Order Service
        OrderDto order = orderClient.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + orderId);
        }

        // Build Delivery entity
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setAgentId(dto.getAgentId());
        delivery.setStatus(DeliveryStatus.IN_PROGRESS); // or use dto.getStatus() if needed
        delivery.setEstimatedTimeOfArrival(LocalDateTime.now().plusHours(1));

        // Save and build response
        Delivery saved = deliveryRepository.save(delivery);

        DeliveryDto response = new DeliveryDto(
                saved.getAgentId(),
                saved.getOrderId(),
                saved.getStatus(),
                saved.getEstimatedTimeOfArrival(),
                order
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DeliveryDto> trackDelivery(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId);
        if (delivery == null) {
            throw new ResourceNotFoundException("Delivery not found for order ID: " + orderId);
        }

        OrderDto order = orderClient.getOrderById(orderId);

        DeliveryDto dto = new DeliveryDto(
                delivery.getAgentId(),
                delivery.getOrderId(),
                delivery.getStatus(),
                delivery.getEstimatedTimeOfArrival(),
                order
        );

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findAll();

        // Collect unique order IDs
        List<Long> orderIds = deliveries.stream()
                .map(Delivery::getOrderId)
                .distinct()
                .collect(Collectors.toList());

        // Map orderId -> OrderDto (multiple Feign calls)
        Map<Long, OrderDto> orderMap = new HashMap<>();
        for (Long orderId : orderIds) {
            orderMap.put(orderId, orderClient.getOrderById(orderId));
        }

        List<DeliveryDto> dtos = deliveries.stream().map(delivery -> {
            OrderDto order = orderMap.get(delivery.getOrderId());
            return new DeliveryDto(
                    delivery.getAgentId(),
                    delivery.getOrderId(),
                    delivery.getStatus(),
                    delivery.getEstimatedTimeOfArrival(),
                    order
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    @Override
    public ResponseEntity<DeliveryDto> updateDeliveryStatus(Long deliveryId, String status) {

        // Fetch the delivery or throw exception
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with ID: " + deliveryId));

        // Convert status string to enum
        DeliveryStatus newStatus = DeliveryStatus.valueOf(status.toUpperCase());

        // Prevent changing status if already DELIVERED
        if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("Delivery is already marked as DELIVERED");
        }

        // Prevent invalid transition from IN_PROGRESS to anything other than DELIVERED
        if (delivery.getStatus() == DeliveryStatus.IN_PROGRESS && newStatus != DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("Can only change from IN_PROGRESS to DELIVERED");
        }

        // Update and save delivery status
        delivery.setStatus(newStatus);
        Delivery updated = deliveryRepository.save(delivery);

        // Update order status if delivery is delivered
        if (newStatus == DeliveryStatus.DELIVERED) {
            orderClient.updateOrderStatus(updated.getOrderId(), "DELIVERED");
        }

        // Fetch order details
        OrderDto order = orderClient.getOrderById(updated.getOrderId());

        // Build response
        DeliveryDto dto = new DeliveryDto(
                updated.getAgentId(),
                updated.getOrderId(),
                updated.getStatus(),
                updated.getEstimatedTimeOfArrival(),
                order
        );

        return ResponseEntity.ok(dto);
    }


}
