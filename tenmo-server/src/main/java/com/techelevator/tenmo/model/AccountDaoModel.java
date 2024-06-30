package com.techelevator.tenmo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The Account class is modeled from the PostgresSQL database
 
*/
 @Entity
@Table(name = "account")
@Getter
@Setter
public class AccountDaoModel {
    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    private Integer accountId;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false, precision = 13, scale = 2)
    private BigDecimal balance;
}
