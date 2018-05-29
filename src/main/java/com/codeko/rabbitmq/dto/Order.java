package com.codeko.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private String orderNumber;
    private String productId;
    private double amount;
    private String note;
}
