package com.techelevator.tenmo.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private Integer accountId;
    private User user;
    private BigDecimal balance;
}
