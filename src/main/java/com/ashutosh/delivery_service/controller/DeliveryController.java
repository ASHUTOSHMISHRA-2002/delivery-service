package com.ashutosh.delivery_service.controller;

import com.ashutosh.delivery_service.dto.DeliveryDto;
import com.ashutosh.delivery_service.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/assign")
    public ResponseEntity<DeliveryDto> assign(@RequestBody DeliveryDto dto) {
        return deliveryService.assignAgent(dto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<DeliveryDto> track(@PathVariable Long orderId) {
        return deliveryService.trackDelivery(orderId);
    }
    @GetMapping("/getalldeliveries")
    public ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }
}
