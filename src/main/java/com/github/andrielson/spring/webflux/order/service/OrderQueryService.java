package com.github.andrielson.spring.webflux.order.service;

import com.github.andrielson.spring.webflux.order.dto.PurchaseOrderResponseDto;
import com.github.andrielson.spring.webflux.order.repository.PurchaseOrderRepository;
import com.github.andrielson.spring.webflux.order.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Service
public class OrderQueryService {
    private final PurchaseOrderRepository orderRepository;

    public Flux<PurchaseOrderResponseDto> getProductsByUserId(int userId) {
        return Flux.fromStream(() -> this.orderRepository.findByUserId(userId).stream())
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
