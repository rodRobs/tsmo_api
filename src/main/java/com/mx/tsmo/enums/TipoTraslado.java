package com.mx.tsmo.enums;

public enum TipoTraslado {

    LOCAL(1), FORANEO(2);

    private int value;

    TipoTraslado(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
