package com.github.andrielson.spring.webflux.order.controller;

import com.github.andrielson.spring.webflux.order.dto.PurchaseOrderRequestDto;
import com.github.andrielson.spring.webflux.order.dto.PurchaseOrderResponseDto;
import com.github.andrielson.spring.webflux.order.service.OrderFulfillmentService;
import com.github.andrielson.spring.webflux.order.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final OrderFulfillmentService fulfillmentService;

    private final OrderQueryService queryService;

    @PostMapping
    public Mono<PurchaseOrderResponseDto> order(@RequestBody Mono<PurchaseOrderRequestDto> requestDtoMono) {
        return this.fulfillmentService.processOrder(requestDtoMono);
    }

    @GetMapping("user/{userId}")
    public Flux<PurchaseOrderResponseDto> getOrdersByUserId(@PathVariable int userId) {
        return this.queryService.getProductsByUserId(userId);
    }

}
