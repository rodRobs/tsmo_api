package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class DetalleDto {

    private String Identificador;
    private String Contenido;
    private double ValorDeclarado;
    private DimensionesDto Dimensiones;

    public String toString() {
        return "Identificador: "+ Identificador +
                " Contenido: " + Contenido +
                " ValorDeclarado: " + ValorDeclarado +
                " Dimensiones: " + Dimensiones.toString();
    }

}
