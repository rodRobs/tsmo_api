package com.mx.tsmo.enums;

public enum TipoClase {

    CARGALTLFORANEO(1), CARGAPESADAFORANEO(2), CARGANORMALFORANEO(3),
    CARGALTLLOCAL(4), CARGAPESADALOCAL(5), CARGANORMALLOCAL(6);

    private int value;

    TipoClase(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
