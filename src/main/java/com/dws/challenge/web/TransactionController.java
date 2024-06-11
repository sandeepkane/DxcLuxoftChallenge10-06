package com.dws.challenge.web;

import com.dws.challenge.domain.Transaction;
import com.dws.challenge.service.FundTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/transfer")
@Slf4j
public class TransactionController {
    private final FundTransferService fundTransferService;

    @Autowired
    public TransactionController(FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
    }

    @PostMapping("/from/{sourceAccountId}/to/{destinationAccountId}/amount/{amount}")
    public ResponseEntity<Transaction> fundTransfer(@PathVariable String sourceAccountId, @PathVariable String destinationAccountId, @PathVariable BigDecimal amount) {
        Transaction transaction = fundTransferService.fundTransfer(sourceAccountId, destinationAccountId, amount);
        return ResponseEntity.ok(transaction);
    }
}
