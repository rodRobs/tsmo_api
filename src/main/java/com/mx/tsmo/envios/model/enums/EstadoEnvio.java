package com.mx.tsmo.envios.model.enums;

public enum EstadoEnvio {

    PENDIENTE(0),
    RECOLECCION(1),
    ALMACEN(2),
    TRANSITO(3),
    ENTREGADO(4),
    PRIMERA_ENTREGA_SIN_EXITO(5),
    SEGUNDA_ENTREGA_SIN_EXITO(6),
    TERCERA_ENTREGA_SIN_EXITO(7),
    DEVUELTO(8),
    CANCELADO(9);

    private final int value;

    private EstadoEnvio(int value) {
        this.value = value;
    }
}
