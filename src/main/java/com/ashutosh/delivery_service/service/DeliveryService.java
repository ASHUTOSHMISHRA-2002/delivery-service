package com.ashutosh.delivery_service.service;

import com.ashutosh.delivery_service.dto.DeliveryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DeliveryService {
    ResponseEntity<DeliveryDto> assignAgent(DeliveryDto dto);
    ResponseEntity<DeliveryDto> trackDelivery(Long orderId);
	ResponseEntity<List<DeliveryDto>> getAllDeliveries();
//	ResponseEntity<DeliveryDto> trackDelivery(Long orderId);
}
