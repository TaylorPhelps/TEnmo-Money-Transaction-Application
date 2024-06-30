package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferTypeRepository extends JpaRepository<TransferType, Integer> {
    @Query("SELECT tt FROM TransferType tt JOIN Transfer t ON tt.transferTypeId = t.transferType.transferTypeId WHERE t.transferId = :transferId")
    TransferType getTransferTypeByTransferTypeId(@Param("transferId") Integer transferId);
}
