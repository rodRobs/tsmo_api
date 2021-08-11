package com.mx.tsmo.cobertura.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CoberturaResponse {

    private String clave;
    private String tipoServicio;
    private String zona;
    private Boolean isOcurre;
    private Boolean isDomicilio;

}
