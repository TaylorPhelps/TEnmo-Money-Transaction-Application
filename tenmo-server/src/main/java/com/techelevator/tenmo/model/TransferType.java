package com.techelevator.tenmo.model;

import lombok.*;
import javax.persistence.*;

/**
 * TransferType is modeled off of the table database/tenmo.sql.
 * This will allow us to get data from our TEnmo PostgreSQL database.
 *  @author Ja'Michael Garcia
 *  @version 1.0
 *  @since 2024-06-04
 */
@Data
@Entity
@Table(name = "transfer_type")
public class TransferType {
    public enum Type{
        REQUEST(1),
        SEND(2);

        public String toString(){
            switch (this) {
                case REQUEST: return "Request";
                case SEND: return "Send";
            }
            return null;
        }
        public final int value;
        private Type(int value){
            this.value = value;
        }
        
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_type_id", nullable = false)
    private Integer transferTypeId;

    @Column(name = "transfer_type_desc", nullable = false, length = 10)
    private String transferTypeDesc;
}
