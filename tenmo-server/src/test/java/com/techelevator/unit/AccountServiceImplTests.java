package com.techelevator.unit;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AccountServiceImplTests {
    private Account account;
    private User user;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        account = new Account();
        user = new User();
        user.setId(1001);
        account.setAccountId(2001);
        account.setBalance(new BigDecimal("1000.00"));
        account.setUser(user);
    }

    @Test
    public void getAccountBalance_WhenUserIdIsValid_ShouldReturnAccountBalance() {
        when(accountRepository.getAccountsByBalanceNotNullAndUser(user.getId())).thenReturn(account.getBalance());
        BigDecimal accountServiceBalance = accountService.getTotalBalance(1001);

        assertNotNull(accountServiceBalance);
        assertEquals(account.getBalance(), accountServiceBalance);
    }

    @Test
    public void getAccountByAccountId_WhenUserIdIsValid_ShouldReturnAccount() {
        when(accountRepository.getAccountByAccountId(2001)).thenReturn(account);

        Account accountByAccountId = accountService.getAccountByAccountId(2001);
        assertNotNull(accountByAccountId);
        assertEquals(account.getAccountId(), accountByAccountId.getAccountId());
        assertEquals(account.getBalance(), accountByAccountId.getBalance());
        assertEquals(account.getUser(), accountByAccountId.getUser());
    }

    @Test
    public void updateBalanceByAccountId_VerifyParameters_BeforeUpdating_ShouldUpdateAccountBalance() {
        BigDecimal updatedBalanced =  new BigDecimal("900.00");
        doNothing().when(accountRepository).updateBalanceByAccountId(any(Integer.class), any(BigDecimal.class));
        accountService.updateBalanceByAccountId(account.getAccountId(), updatedBalanced);
        verify(accountRepository).updateBalanceByAccountId(account.getAccountId(), updatedBalanced);
    }
}
