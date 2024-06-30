package com.techelevator.unit;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.repository.TransferTypeRepository;
import com.techelevator.tenmo.service.TransferTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TransferTypeServiceImplTests {

    @Mock
    private TransferTypeRepository transferTypeRepository;

    @InjectMocks
    private TransferTypeServiceImpl transferTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getTransferType_WithTransferId_ReturnsValidTransferType() {
        TransferType transferType = new TransferType();
        transferType.setTransferTypeId(TransferType.Type.SEND.ordinal());
        transferType.setTransferTypeDesc(TransferType.Type.SEND.name());

        Transfer transfer = new Transfer();
        transfer.setTransferType(transferType);
        transfer.setTransferId(3001);

        when(transferTypeRepository.getTransferTypeByTransferTypeId(3001)).thenReturn(transferType);

        TransferType serviceType = transferTypeService.getTransferTypeByTransferTypeId(transfer.getTransferId());
        assertNotNull(serviceType);
        assertEquals(transferType, serviceType);
        assertEquals(TransferType.Type.SEND.ordinal(), serviceType.getTransferTypeId());
        assertEquals(TransferType.Type.SEND.name(), serviceType.getTransferTypeDesc());

    }

    @Test
    void getTransferType_WithInvalidTransferId_ReturnsNullTransferType() {
        when(transferTypeRepository.getTransferTypeByTransferTypeId(3001)).thenReturn(null);
        TransferType serviceType = transferTypeService.getTransferTypeByTransferTypeId(3001);
        
        assertNull(serviceType);
    }
}
