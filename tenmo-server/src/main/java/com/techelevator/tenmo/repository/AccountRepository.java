package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
/**
 * Repository for TEnmo users.
 * Comes with the default methods that JPA provides.
 * Currently, it has only one custom query
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Gets the total balance from all the user's accounts.
     * @param userId The user to look for
     * @return The total amount that the user has in their account
     */
    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.user.id = :userId")
    BigDecimal getAccountsByBalanceNotNullAndUser(@Param("userId") Integer userId);

//    @Query( value =  "SELECT balance FROM Account WHERE account_id = :accountId", nativeQuery = true)
//    BigDecimal getAccountBalanceByAccountId(@Param("accountId") Integer accountId);

    Account getAccountByAccountId(Integer accountId);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = :balance WHERE a.accountId = :accountId")
    void updateBalanceByAccountId(@Param("accountId") Integer accountId, @Param("balance") BigDecimal balance);
}
