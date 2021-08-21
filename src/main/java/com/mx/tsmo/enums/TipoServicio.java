package com.mx.tsmo.enums;

public enum TipoServicio {

    ORDINARIO("O"),
    RECOLECCION_A_DOMICILIO("RDO");


    private String value;

    TipoServicio(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
