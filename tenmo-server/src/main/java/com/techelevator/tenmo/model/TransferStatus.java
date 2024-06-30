package com.techelevator.tenmo.model;

import lombok.Data;

import javax.persistence.*;

/**
 * TransferStatus is an object that is modeled from a table
 * in the TEnmo database to read in the data using JPA.
 *  @author Ja'Michael Garcia
 *  @version 1.0
 *  @since 2024-06-04
 */
@Data
@Entity
@Table(name = "transfer_status")
public class TransferStatus {
    
    public enum Status {
        PENDING(1),
        APPROVED(2),
        REJECTED(3);
        public String toString(){
            switch (this) {
                case APPROVED: return "Approved";
                case PENDING: return "Pending";
                case REJECTED: return "Rejected";
            }
            return null;
        }
        public final int value;
        private Status(int value){
            this.value = value;
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_status_id", nullable = false)
    private Integer transferStatusId;

    @Column(name = "transfer_status_desc", nullable = false, length = 10)
    private String transferStatusDesc;
}
