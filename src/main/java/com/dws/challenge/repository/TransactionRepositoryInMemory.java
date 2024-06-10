package com.dws.challenge.repository;

import com.dws.challenge.domain.Transaction;
import com.dws.challenge.exception.DuplicateTransactionIdException;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepositoryInMemory implements TransactionRepository {
    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public void addFundTransferTransaction(Transaction transaction) {
        Transaction previousTransaction = transactions.putIfAbsent(transaction.getTransactionId(), transaction);
        if (previousTransaction != null)
            throw new DuplicateTransactionIdException("C4001","Transaction Id " + transaction.getTransactionId() + " already exists!");
    }

    @Override
    public void clearTransactions() {
        transactions.clear();
    }
}
