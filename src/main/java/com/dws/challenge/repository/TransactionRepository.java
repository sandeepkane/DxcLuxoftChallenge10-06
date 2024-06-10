package com.dws.challenge.repository;

import com.dws.challenge.domain.Transaction;

public interface TransactionRepository {
    void addFundTransferTransaction(Transaction transaction);

    void clearTransactions();
}
