package com.techelevator.integration;

import com.techelevator.tenmo.TenmoApplication;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TenmoApplication.class)
public class AccountRepositoryIntegrationTest {
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        User user = new User();
        user.setId(1001);
        user.setUsername("Test");
        user.setPassword("password");

        Account account = new Account();
        account.setAccountId(2001);
        account.setBalance(BigDecimal.valueOf(1000.00));
        account.setUser(user);
        accountRepository.save(account);
    }

    @Test
    public void testGettingAccountById() {
        Account account = accountRepository.getAccountByAccountId(2001);
        assertEquals(account.getAccountId(), 2001);
        assertEquals(account.getBalance(), new BigDecimal("1000.00"));
    }

    @Test
    public void testUpdatingAccountBalanceByAccountId() {
        accountRepository.updateBalanceByAccountId(2001, BigDecimal.valueOf(2175));
        Account account = accountRepository.getAccountByAccountId(2001);
        assertEquals(account.getBalance(), new BigDecimal("2175.00"));
    }
}
