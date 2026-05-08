package com.example.ecomm.service;

import com.example.ecomm.model.Order;
import com.example.ecomm.model.OrderItem;
import com.example.ecomm.model.Product;
import com.example.ecomm.model.dto.OrderItemRequest;
import com.example.ecomm.model.dto.OrderItemResponse;
import com.example.ecomm.model.dto.OrderRequest;
import com.example.ecomm.model.dto.OrderResponse;
import com.example.ecomm.repo.OrderRepo;
import com.example.ecomm.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;

    public OrderResponse addOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String orderId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(orderId);
        order.setStatus("Placed");
        order.setCustomerName(orderRequest.customerName());
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerEmail(orderRequest.customerMail());
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemReq : orderRequest.items()) {
            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));
            product.setQuantity(product.getQuantity() - itemReq.quantity());
            productRepo.save(product);
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .order(order)
                    .price(
                            BigDecimal.valueOf(product.getPrice())
                                    .multiply(BigDecimal.valueOf(itemReq.quantity()))
                    )
                    .build();
            orderItems.add(orderItem);

        }
    order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);
        List<OrderItemResponse> response =new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    orderItem.getProduct().getName(),
                    orderItem.getQuantity(),
                    orderItem.getPrice()
            );
            response.add(orderItemResponse);
        }
        return new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getCustomerEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate().toLocalDate(),
                response
        );

    }
    @Transactional
    public OrderResponse getOrderById(Integer orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        List<OrderItemResponse> orderItems = new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItems()) {

            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    orderItem.getProduct().getName(),
                    orderItem.getQuantity(),
                    orderItem.getPrice()
            );

            orderItems.add(orderItemResponse);
        }

        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getStatus(),
                order.getOrderDate().toLocalDate(),
                orderItems
        );
    }
    @Transactional
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepo.findAll();

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
        List<OrderItemResponse> items = new ArrayList<>();
            for(OrderItem orderItem : order.getOrderItems()) {
                OrderItemResponse orderItemResponse = new OrderItemResponse(
                        orderItem.getProduct().getName(),
                        orderItem.getQuantity(),
                        orderItem.getPrice()
                );
                items.add(orderItemResponse);

            }
            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getCustomerEmail(),
                    order.getStatus(),
                    order.getOrderDate().toLocalDate(),
                    items
            );
            orderResponses.add(orderResponse);
        }
return orderResponses;
    }

    @Transactional
    public void updateOrder(
            Integer orderId,
            OrderRequest orderRequest
    ) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order Not Found"));

        for (OrderItem oldItem : order.getOrderItems()) {

            Product product = oldItem.getProduct();

            product.setQuantity(
                    product.getQuantity() + oldItem.getQuantity()
            );

            productRepo.save(product);
        }


        order.getOrderItems().clear();


        order.setCustomerName(orderRequest.customerName());
        order.setCustomerEmail(orderRequest.customerMail());
        order.setStatus("UPDATED");
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> updatedItems = new ArrayList<>();

        for (OrderItemRequest itemReq : orderRequest.items()) {

            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() ->
                            new RuntimeException("Product Not Found"));


            if (product.getQuantity() < itemReq.quantity()) {
                throw new RuntimeException(
                        "Insufficient stock"
                );
            }


            product.setQuantity(
                    product.getQuantity() - itemReq.quantity()
            );

            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .order(order)
                    .price(
                            BigDecimal.valueOf(product.getPrice())
                                    .multiply(
                                            BigDecimal.valueOf(
                                                    itemReq.quantity()
                                            )
                                    )
                    )
                    .build();

            updatedItems.add(orderItem);
        }

        order.setOrderItems(updatedItems);

        orderRepo.save(order);
    }
}
