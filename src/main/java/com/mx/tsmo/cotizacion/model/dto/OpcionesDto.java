package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class OpcionesDto {

    private String TipoEnvio;
    private String TipoEntrega;
    private String TipoServicio;
    private String TipoCobro;

    public String toString() {
        return "TipoEnvio: " + TipoEnvio +
                " TipoEntrega: " + TipoEntrega +
                " TipoServicio: " + TipoServicio +
                " TipoCobro: " + TipoCobro;
    }

}
