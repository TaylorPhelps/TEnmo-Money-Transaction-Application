package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.AccountDaoModel;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;
    
    public JdbcAccountDao (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AccountDaoModel getAccountById(Integer accountId){
        
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql,
            new BeanPropertyRowMapper<AccountDaoModel>(AccountDaoModel.class), accountId);
            
            
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        
    }

    @Override
    public BigDecimal getBalanceByAccountId(Integer accountId) {
        // TODO Auto-generated method st
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql,BigDecimal.class, accountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public AccountDaoModel updateAccount(AccountDaoModel account) {
        
        AccountDaoModel updatedAccount = null;
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, account.getBalance(), account.getAccountId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedAccount = getAccountById(account.getAccountId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedAccount;
    }

    @Override
    public Integer getAccountIdByUserId(Integer userId) {
        
        
        String sql = "SELECT account_id FROM account WHERE user_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql,Integer.class, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public AccountDaoModel setAccountBalance(int accountId, BigDecimal balance){
        AccountDaoModel updatedAccount = null;
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, balance, accountId);
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedAccount = getAccountById(accountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedAccount;
    }

    
    
}
