package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    public JdbcTransferDao (JdbcTemplate jdbcTemplate, AccountDao account) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = account;
    }

    public Transfer createTransfer(@Valid Transfer transfer) {
        
        String sql = 
        "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        try {
            int transferId = jdbcTemplate.queryForObject(sql, int.class, 
                transfer.getTransferTypeId(),
                transfer.getTransferStatusId(),
                transfer.getAccountFromId(),
                transfer.getAccountToId(),
                transfer.getAmount()
            );
            return getTransferById(transferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
    
    public Transfer getTransferById(Integer transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()){
                return mapRowToTransfer(results);
            }
            return null;            
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer cc = new Transfer();
        cc.setAccountFromId(rs.getInt("account_from"));
        cc.setAccountToId(rs.getInt("account_to"));
        cc.setAmount(new BigDecimal(rs.getString("amount")));
        cc.setTransferId(rs.getInt("transfer_id"));
        cc.setTransferStatusId(rs.getInt("transfer_status_id"));
        cc.setTransferTypeId(rs.getInt("transfer_type_id"));
        return cc;
    }

    @Override
    public Transfer updateTransferStatus(int transferId, int transferStatusId) {
        Transfer updatedTransfer = null;
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, transferStatusId, transferId);
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedTransfer = getTransferById(transferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTransfer;
    }
    
}
