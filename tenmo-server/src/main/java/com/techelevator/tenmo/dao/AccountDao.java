package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.AccountDaoModel;

public interface AccountDao {
    BigDecimal getBalanceByAccountId(Integer accountId);
    AccountDaoModel getAccountById(Integer accountId);
    AccountDaoModel updateAccount(AccountDaoModel account);
    AccountDaoModel setAccountBalance(int accountId, BigDecimal balance);
    Integer getAccountIdByUserId(Integer userId);
    
}
