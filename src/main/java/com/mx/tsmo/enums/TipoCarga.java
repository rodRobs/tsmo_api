package com.mx.tsmo.enums;

public enum TipoCarga {

    NORMAL(1), PESADO(2), LTL(3);

    private int value;

    TipoCarga(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
