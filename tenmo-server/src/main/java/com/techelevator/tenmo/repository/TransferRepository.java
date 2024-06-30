package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {
    @Query("SELECT t FROM Transfer t JOIN Account a ON t.accountFromId = a.accountId JOIN User u ON a.user.id = u.id WHERE u.id = :userId")
    List<Transfer> getAllByAccountFromByUserId(@Param("userId") Integer userId);

    @Query("SELECT t FROM Transfer t JOIN Account a ON t.accountToId = a.accountId JOIN User u ON a.user.id = u.id WHERE u.id = :userId")
    List<Transfer> getAllByAccountToByUserId(@Param("userId") Integer userId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Transfer t SET t.s")
//    void updateTransferByTransferId(Transfer transfer);
}
