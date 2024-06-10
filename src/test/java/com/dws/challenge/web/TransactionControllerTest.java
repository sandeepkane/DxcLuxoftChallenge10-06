package com.dws.challenge.web;

import com.dws.challenge.domain.Account;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.FundTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class TransactionControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private AccountsService accountsService;
    @Autowired
    private FundTransferService fundTransferService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void prepareMockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void transferFund_Success() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-124\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/transfer/from/Id-123/to/Id-124/amount/200")).andExpect(status().isOk());

        Account senderAccount = accountsService.getAccount("Id-123");
        Account receiverAccount = accountsService.getAccount("Id-124");
        assertThat(senderAccount.getBalance()).isEqualByComparingTo("800");
        assertThat(receiverAccount.getBalance()).isEqualByComparingTo("1200");
    }

    @Test
    void transferFund_InsufficientBalance() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-123\",\"balance\":100}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-124\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/transfer/from/Id-123/to/Id-124/amount/200")).andExpect(status().isBadRequest());

        Account senderAccount = accountsService.getAccount("Id-123");
        Account receiverAccount = accountsService.getAccount("Id-124");
        assertThat(senderAccount.getBalance()).isEqualByComparingTo("100");
        assertThat(receiverAccount.getBalance()).isEqualByComparingTo("1000");
    }

    @Test
    void transferFund_InvalidSenderAccount() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-124\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/transfer/from/Id-125/to/Id-124/amount/200")).andExpect(status().isBadRequest());

        Account senderAccount = accountsService.getAccount("Id-123");
        Account receiverAccount = accountsService.getAccount("Id-124");
        assertThat(senderAccount.getBalance()).isEqualByComparingTo("1000");
        assertThat(receiverAccount.getBalance()).isEqualByComparingTo("1000");
    }

    @Test
    void transferFund_InvalidReceiverAccount() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-124\",\"balance\":1000}")).andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/transfer/from/Id-123/to/Id-125/amount/200")).andExpect(status().isBadRequest());

        Account senderAccount = accountsService.getAccount("Id-123");
        Account receiverAccount = accountsService.getAccount("Id-124");
        assertThat(senderAccount.getBalance()).isEqualByComparingTo("1000");
        assertThat(receiverAccount.getBalance()).isEqualByComparingTo("1000");
    }
}
