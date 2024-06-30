package com.techelevator.tenmo.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Client side Transfer class used for REST API calls.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-09
 */
@Data
public class Transfer
{
    private Integer transferId;
    private Integer transferTypeId;
    private Integer transferStatusId;
    private Integer accountFromId;
    private Integer accountToId;
    private Integer userFromId;
    private Integer userToId;
    private BigDecimal amount;
}
