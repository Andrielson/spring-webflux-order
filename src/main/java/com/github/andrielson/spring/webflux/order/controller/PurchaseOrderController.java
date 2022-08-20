package com.github.andrielson.spring.webflux.order.controller;

import com.github.andrielson.spring.webflux.order.dto.PurchaseOrderRequestDto;
import com.github.andrielson.spring.webflux.order.dto.PurchaseOrderResponseDto;
import com.github.andrielson.spring.webflux.order.service.OrderFulfillmentService;
import com.github.andrielson.spring.webflux.order.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final OrderFulfillmentService fulfillmentService;

    private final OrderQueryService queryService;

    @PostMapping
    public Mono<ResponseEntity<PurchaseOrderResponseDto>> order(@RequestBody Mono<PurchaseOrderRequestDto> requestDtoMono) {
        return this.fulfillmentService.processOrder(requestDtoMono)
                .map(ResponseEntity::ok)
                .onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
                .onErrorReturn(WebClientRequestException.class, ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    @GetMapping("user/{userId}")
    public Flux<PurchaseOrderResponseDto> getOrdersByUserId(@PathVariable int userId) {
        return this.queryService.getProductsByUserId(userId);
    }

}
