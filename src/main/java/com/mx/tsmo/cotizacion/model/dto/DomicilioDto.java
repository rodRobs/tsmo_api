package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class DomicilioDto {

    private String Pais;
    private String Estado;
    private String Ciudad;
    private String Colonia;
    private String CodigoPostal;
    private String Calle;
    private String NumeroInt;
    private String NumeroExt;

    public String toString() {
        return "Pais: " + Pais +
                " Estado: " + Estado +
                " Ciudad: " + Ciudad +
                " Colonia: " + Colonia +
                " CodigoPostal: " + CodigoPostal +
                " Calle: " + Calle +
                " NumeroInt: " + NumeroInt +
                " NumeroExt: " + NumeroExt;
    }

}
