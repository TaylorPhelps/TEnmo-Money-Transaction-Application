package com.techelevator.tenmo;

public enum TransferTypes {
    REQUEST(1),
    SEND(2);
    public final int value;
    private TransferTypes(int value){
        this.value = value;
    }
}
