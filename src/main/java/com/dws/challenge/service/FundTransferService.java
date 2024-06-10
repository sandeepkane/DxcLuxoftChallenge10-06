package com.dws.challenge.service;

import com.dws.challenge.domain.Transaction;

import java.math.BigDecimal;

public interface FundTransferService {
    Transaction fundTransfer(String sourceAccount, String destinationAccount, BigDecimal amountToBeTransferred);
}
