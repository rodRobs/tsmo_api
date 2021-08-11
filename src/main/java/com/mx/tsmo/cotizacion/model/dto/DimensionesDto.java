package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DimensionesDto {

    private short Largo;
    private short Alto;
    private short Ancho;
    private short Peso;

    public String toString() {
        return "Largo: " + Largo +
                " Alto: " + Alto +
                " Ancho: " + Ancho +
                " Peso: " + Peso;
    }

}
