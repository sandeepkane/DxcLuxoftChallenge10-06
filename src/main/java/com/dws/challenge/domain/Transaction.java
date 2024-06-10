package com.dws.challenge.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private String transactionId;
    private String senderAccountId;
    private String receiverAccountId;
    private BigDecimal transactionAmount;
}
