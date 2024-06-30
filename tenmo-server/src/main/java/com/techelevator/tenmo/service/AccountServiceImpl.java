package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service implementation for the Account entity.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Uses the JPA account repo to get the total balance for a user
     * @param userId The user to look for
     * @return The total amount that the user has tied to their account.
     */
    @Override
    public BigDecimal getTotalBalance(Integer userId) {
        return accountRepository.getAccountsByBalanceNotNullAndUser(userId);
    }

    @Override
    public Account getAccountByAccountId(Integer userId) {
        return accountRepository.getAccountByAccountId(userId);
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void updateBalanceByAccountId(Integer accountId, BigDecimal balance) {
        accountRepository.updateBalanceByAccountId(accountId, balance);
    }

}
