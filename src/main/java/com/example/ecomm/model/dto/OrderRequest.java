package com.example.ecomm.model.dto;

import java.util.List;

public record OrderRequest(
        String customerName,
        String customerMail,
        List<OrderItemRequest> items
) { }
