package com.techelevator.tenmo.model;

import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transfer_id")
    @SequenceGenerator(name = "seq_transfer_id", sequenceName = "seq_transfer_id", allocationSize = 1)
    @Column(name = "transfer_id")
    private Integer transferId;

    @ManyToOne
    @JoinColumn(name = "transfer_type_id", nullable = false)
    private TransferType transferType;

    @ManyToOne
    @JoinColumn(name = "transfer_status_id", nullable = false)
    private TransferStatus transferStatus;

    @Transient
    private Integer transferTypeId;

    @Transient
    private Integer transferStatusId;
    
    @Column(name = "account_from", nullable = false)
    @NotNull
    private Integer accountFromId;
    
    @Column(name = "account_to", nullable = false)
    @NotNull
    private Integer accountToId;
    
    @DecimalMin(value = "0.0", inclusive = false)
    @NotNull
    private BigDecimal amount;

}
