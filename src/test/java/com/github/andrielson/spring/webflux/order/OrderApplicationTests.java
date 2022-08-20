package com.github.andrielson.spring.webflux.order;

import com.github.andrielson.spring.webflux.order.client.ProductClient;
import com.github.andrielson.spring.webflux.order.client.UserClient;
import com.github.andrielson.spring.webflux.order.dto.ProductDto;
import com.github.andrielson.spring.webflux.order.dto.PurchaseOrderRequestDto;
import com.github.andrielson.spring.webflux.order.dto.UserDto;
import com.github.andrielson.spring.webflux.order.service.OrderFulfillmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class OrderApplicationTests {

    @Autowired
    private OrderFulfillmentService fulfillmentService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ProductClient productClient;

    @Test
    void contextLoads() {
        var dtoFlux = Flux.zip(userClient.getAllUsers(), productClient.getAllProducts())
                .map(t -> buildDto(t.getT1(), t.getT2()))
                .flatMap(dto -> this.fulfillmentService.processOrder(Mono.just(dto)))
                .doOnNext(System.out::println);

        StepVerifier.create(dtoFlux)
                .expectNextCount(4)
                .verifyComplete();
    }

    private PurchaseOrderRequestDto buildDto(UserDto userDto, ProductDto productDto) {
        var dto = new PurchaseOrderRequestDto();
        dto.setUserId(userDto.getId());
        dto.setProductId(productDto.getId());
        return dto;
    }

}
