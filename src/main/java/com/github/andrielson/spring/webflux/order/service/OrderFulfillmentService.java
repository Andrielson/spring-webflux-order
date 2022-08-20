package com.github.andrielson.spring.webflux.order.service;

import com.github.andrielson.spring.webflux.order.client.ProductClient;
import com.github.andrielson.spring.webflux.order.client.UserClient;
import com.github.andrielson.spring.webflux.order.dto.PurchaseOrderRequestDto;
import com.github.andrielson.spring.webflux.order.dto.PurchaseOrderResponseDto;
import com.github.andrielson.spring.webflux.order.dto.RequestContext;
import com.github.andrielson.spring.webflux.order.repository.PurchaseOrderRepository;
import com.github.andrielson.spring.webflux.order.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Service
public class OrderFulfillmentService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final ProductClient productClient;

    private final UserClient userClient;

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {
        return requestDtoMono.map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getPurchaseOrder)
                .map(this.purchaseOrderRepository::save)
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<RequestContext> productRequestResponse(RequestContext context) {
        return this.productClient.getProductById(context.getPurchaseOrderRequestDto().getProductId())
                .doOnNext(context::setProductDto)
                .thenReturn(context);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext context) {
        return this.userClient.authorizeTransaction(context.getTransactionRequestDto())
                .doOnNext(context::setTransactionResponseDto)
                .thenReturn(context);
    }

}
