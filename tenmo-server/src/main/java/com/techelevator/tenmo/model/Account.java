package com.techelevator.tenmo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The Account class is modeled from the PostgresSQL database to
 * collect the data using JPA
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-04
 */
@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_account_id")
    @SequenceGenerator(name = "seq_account_id", sequenceName = "seq_account_id", allocationSize = 1)
    @Column(name = "account_id", nullable = false, unique = true)
    private Integer accountId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "balance", nullable = false, precision = 13, scale = 2)
    private BigDecimal balance;
}
