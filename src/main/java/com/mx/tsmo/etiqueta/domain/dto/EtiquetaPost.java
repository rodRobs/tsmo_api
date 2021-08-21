package com.mx.tsmo.etiqueta.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class EtiquetaPost {

    private String Guia;
    private int TipoImpresion;

}
