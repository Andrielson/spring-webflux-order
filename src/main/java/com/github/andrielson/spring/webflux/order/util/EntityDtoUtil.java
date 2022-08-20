package com.github.andrielson.spring.webflux.order.util;

import com.github.andrielson.spring.webflux.order.dto.*;
import com.github.andrielson.spring.webflux.order.entity.PurchaseOrder;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {
    public static void setTransactionRequestDto(RequestContext requestContext) {
        var dto = new TransactionRequestDto();
        dto.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
        dto.setAmount(requestContext.getProductDto().getPrice());
        requestContext.setTransactionRequestDto(dto);
    }

    public static PurchaseOrder getPurchaseOrder(RequestContext requestContext) {
        var purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(requestContext.getPurchaseOrderRequestDto().getUserId());
        purchaseOrder.setProductId(requestContext.getPurchaseOrderRequestDto().getProductId());
        purchaseOrder.setAmount(requestContext.getProductDto().getPrice());

        var status = requestContext.getTransactionResponseDto().getStatus();
        var orderStatus = TransactionStatus.APPROVED.equals(status) ? OrderStatus.COMPLETED : OrderStatus.FAILED;
        purchaseOrder.setStatus(orderStatus);

        return purchaseOrder;
    }

    public static PurchaseOrderResponseDto getPurchaseOrderResponseDto(PurchaseOrder purchaseOrder) {
        var dto = new PurchaseOrderResponseDto();
        BeanUtils.copyProperties(purchaseOrder, dto);
        dto.setOrderId(purchaseOrder.getId());
        return dto;
    }
}
