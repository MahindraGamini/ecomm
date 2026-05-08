package com.example.ecomm.controller;

import com.example.ecomm.model.Order;
import com.example.ecomm.model.dto.OrderRequest;
import com.example.ecomm.model.dto.OrderResponse;
import com.example.ecomm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RestController
public class OrderController {
    @Autowired

    private OrderService orderService;
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.addOrder(orderRequest);
        return new ResponseEntity<>(orderResponse,HttpStatus.CREATED);

    }
    @GetMapping("/viewOrder")
    public ResponseEntity<OrderResponse> viewOrder(@RequestParam int orderId) {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }
    @GetMapping("/view")
    public ResponseEntity<List<OrderResponse>> viewOrder() {
        List <OrderResponse> orderResponse = orderService.getAllOrders();
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<OrderResponse> updateOrder(@RequestParam Integer orderId,@RequestBody OrderRequest orderRequest) {
        orderService.updateOrder(orderId,orderRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
