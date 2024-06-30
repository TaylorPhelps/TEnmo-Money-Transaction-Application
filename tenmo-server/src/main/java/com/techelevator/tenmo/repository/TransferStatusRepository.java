package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferStatusRepository extends JpaRepository<TransferStatus, Integer> {
    @Query("SELECT ts FROM TransferStatus ts JOIN Transfer t ON ts.transferStatusId = t.transferStatus.transferStatusId WHERE t.transferId = :transferId")
    TransferStatus getTransferStatusName(@Param("transferId") Integer transferId);
}
