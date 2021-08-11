package com.mx.tsmo.enums;

public enum TipoEnvioCorreo {

    ENVIO(1), ALTA_OPERADOR(2);

    private int value;

    TipoEnvioCorreo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
