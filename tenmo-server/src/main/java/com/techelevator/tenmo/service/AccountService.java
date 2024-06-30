package com.techelevator.tenmo.service;

import java.math.BigDecimal;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AccountDaoModel;

/**
 * Our Account service that will allow new implementations
 * and features when needed. Always create new methods
 * within this interface.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
public interface AccountService {
    BigDecimal getTotalBalance(Integer userId);
    Account getAccountByAccountId(Integer userId);
    Account saveAccount(Account account);
    void updateBalanceByAccountId(Integer accountId, BigDecimal balance);
}
