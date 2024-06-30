package com.techelevator.tenmo.service;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Account;
import org.springframework.stereotype.Service;

import com.techelevator.tenmo.dao.AccountDao;

@Service
public class AccountServiceDao implements AccountService {
    
    AccountDao accountDao;
    
    public AccountServiceDao(AccountDao accountDao){
        this.accountDao = accountDao;
    }
    @Override
    public BigDecimal getTotalBalance(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalBalance'");
    }

    @Override
    public Account getAccountByAccountId(Integer userId) {
        return null;
    }

    @Override
    public Account saveAccount(Account account) {
        return null;
    }

    @Override
    public void updateBalanceByAccountId(Integer accountId, BigDecimal balance) {

    }


}
