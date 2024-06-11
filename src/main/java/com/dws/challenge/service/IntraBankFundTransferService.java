package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.Transaction;
import com.dws.challenge.exception.ChallengeAppException;
import com.dws.challenge.exception.InsufficientFundsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class IntraBankFundTransferService implements FundTransferService {
    private final AccountsService accountsService;
    private final NotificationService notificationService;

    @Autowired
    public IntraBankFundTransferService(AccountsService accountsService, NotificationService notificationService) {
        this.accountsService = accountsService;
        this.notificationService = notificationService;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    @Override
    public Transaction fundTransfer(String senderAccountId, String receiverAccountId, BigDecimal amountToBeTransferred) {
        if (amountToBeTransferred.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ChallengeAppException("C4005", "The transfer amount must be positive number.");
        }
        Account senderAccount = accountsService.getAccount(senderAccountId);
        if (null == senderAccount)
            throw new ChallengeAppException("C4004", "Invalid Account. Sender account id " + senderAccountId + " not valid.");
        Account receiverAccount = accountsService.getAccount(receiverAccountId);
        if (null == receiverAccount)
            throw new ChallengeAppException("C4003", "Invalid Account. Receiver account id " + receiverAccountId + " not valid.");
        Transaction transaction = new Transaction();
        /**
         *
         * Thread safe transfer lies here
         * Sender account and receiver account has to be synchronized
         * So other thread wait till transfer is done
         *
         * Transaction creation can be moved out, can be async
         * */
        synchronized (senderAccount) {
            synchronized (receiverAccount) {
                if (!senderAccount.withdraw(amountToBeTransferred))
                    throw new InsufficientFundsException("C4002", senderAccountId + " Insufficient Balance.");
                receiverAccount.deposit(amountToBeTransferred);
                transaction.setTransactionId(UUID.randomUUID().toString());
                transaction.setTransactionAmount(amountToBeTransferred);
                transaction.setSenderAccountId(senderAccountId);
                transaction.setReceiverAccountId(receiverAccountId);
            }
        }
        notificationService.notifyAboutTransfer(senderAccount, "Fund transfer: " + amountToBeTransferred + " from account: " + senderAccountId + " to account: " + receiverAccountId + " is success with TXN ID: " + transaction.getTransactionId() + ". New Balance : " + senderAccount.getBalance().toString());
        return transaction;
    }
}
