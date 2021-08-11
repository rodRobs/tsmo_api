package com.mx.tsmo.envios.model.enums;

public enum DescripcionRastreos {

    RECOLECCION("El paquete se ha recoletado"),
    ALMACEN(" El paquete se encuentra en almacén"),
    TRANSITO(" El paquete ha salido de las instalaciones para entrega en curso"),
    ENTREGADO("El paquete se ha entregado en su destino"),
    ENTREGA_PRIMERA_SIN_EXITO("Se realizó primer intento de entrega del paquete sin éxito"),
    ENTREGA_SEGUNDA_SIN_EXITO("Se realizó segundo intento de entrega del paquete sin éxito"),
    ENTREGA_TERCERA_SIN_EXITO("Se realizó tercer intento de entrega del paquete sin éxito"),
    DEVUELTO("EL paquete se ha devuelto"),
    CANCELADO("El envio del paquete se ha cancelado");

    private final String value;

    private DescripcionRastreos(String value) {
        this.value = value;
    }
}
