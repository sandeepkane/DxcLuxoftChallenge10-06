package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.Transaction;
import com.dws.challenge.exception.ChallengeAppException;
import com.dws.challenge.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class IntraBankFundTransferServiceTest {
    @Mock
    private AccountsService accountsService;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private IntraBankFundTransferService intraBankFundTransferService;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFundTransfer_Success() {
        String senderAccountId = "senderAccountId";
        String receiverAccountId = "receiverAccountId";
        BigDecimal amountToBeTransferred = BigDecimal.TEN;

        Account senderAccount = mock(Account.class);
        Account receiverAccount = mock(Account.class);
        when(accountsService.getAccount(senderAccountId)).thenReturn(senderAccount);
        when(senderAccount.withdraw(amountToBeTransferred)).thenReturn(true);
        when(senderAccount.getBalance()).thenReturn(BigDecimal.TEN);
        when(accountsService.getAccount(receiverAccountId)).thenReturn(receiverAccount);
        doNothing().when(notificationService).notifyAboutTransfer(eq(senderAccount), anyString());

        Transaction transaction = intraBankFundTransferService.fundTransfer(senderAccountId, receiverAccountId, amountToBeTransferred);

        assertNotNull(transaction);
    }

    @Test
    void testFundTransfer_InsufficientBalance() {
        String senderAccountId = "senderAccountId";
        String receiverAccountId = "receiverAccountId";
        BigDecimal amountToBeTransferred = BigDecimal.TEN;

        Account senderAccount = mock(Account.class);
        Account receiverAccount = mock(Account.class);
        when(accountsService.getAccount(senderAccountId)).thenReturn(senderAccount);
        when(senderAccount.withdraw(amountToBeTransferred)).thenReturn(false);

        when(accountsService.getAccount(receiverAccountId)).thenReturn(receiverAccount);

        assertThrows(InsufficientFundsException.class, () -> intraBankFundTransferService.fundTransfer(senderAccountId, receiverAccountId, amountToBeTransferred));
    }

    @Test
    void testFundTransfer_InvalidSenderAccount() {
        String senderAccountId = "senderAccountId";
        String receiverAccountId = "receiverAccountId";
        BigDecimal amountToBeTransferred = BigDecimal.TEN;

        when(accountsService.getAccount(senderAccountId)).thenReturn(null);

        assertThrows(ChallengeAppException.class, () -> intraBankFundTransferService.fundTransfer(senderAccountId, receiverAccountId, amountToBeTransferred));
    }

    @Test
    void testFundTransfer_InvalidReceiverAccount() {
        String senderAccountId = "senderAccountId";
        String receiverAccountId = "receiverAccountId";
        BigDecimal amountToBeTransferred = BigDecimal.TEN;

        Account senderAccount = mock(Account.class);
        when(accountsService.getAccount(senderAccountId)).thenReturn(senderAccount);
        when(accountsService.getAccount(receiverAccountId)).thenReturn(null);

        assertThrows(ChallengeAppException.class, () -> intraBankFundTransferService.fundTransfer(senderAccountId, receiverAccountId, amountToBeTransferred));
    }

    @Test
    void testFundTransfer_InvalidAmount() {
        String senderAccountId = "senderAccountId";
        String receiverAccountId = "receiverAccountId";
        BigDecimal amountToBeTransferred = BigDecimal.ZERO;

        assertThrows(ChallengeAppException.class, () -> intraBankFundTransferService.fundTransfer(senderAccountId, receiverAccountId, amountToBeTransferred));
    }
}
