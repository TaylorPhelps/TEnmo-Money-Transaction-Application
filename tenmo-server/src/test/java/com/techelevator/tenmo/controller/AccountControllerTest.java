package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountController accountController;

    private static String testUserToken = "mocked-token";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.clearContext();

        try {
            when(LoginUtils.getTokenForLogin("test", "test", mockMvc)).thenReturn(testUserToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldPass_getTotalBalance() throws Exception {
        BigDecimal balance = new BigDecimal("1000.00");

        when(accountService.getTotalBalance(anyInt())).thenReturn(balance);

        mockMvc.perform(get("/account/balance")
                        .param("userId", "1001") // Adjust the userId parameter based on your implementation
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(balance));
    }

    @Test
    @WithMockUser("test")
    public void shouldPass_getAccountByAccountId() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setBalance(new BigDecimal("1000.00"));

        //when(accountController.getAccountByAccountId(1, User)).thenReturn(account);

        mockMvc.perform(get("/account/information")
                        .param("accountId", "1")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(account.getAccountId()))
                .andExpect(jsonPath("$.balance").value(account.getBalance()));
    }

    @Test
    public void shouldPass_updateAccountBalance() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setBalance(new BigDecimal("2000.00"));

        doNothing().when(accountService).updateBalanceByAccountId(anyInt(), any(BigDecimal.class));

        mockMvc.perform(put("/account/balance/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account))
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isNoContent());
    }
}
