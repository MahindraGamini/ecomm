package com.example.ecomm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderItem {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
private  Integer id;
 @ManyToOne
private Product product;
private Integer quantity;
private BigDecimal price;
@ManyToOne(fetch = FetchType.LAZY)
private Order order;

}
