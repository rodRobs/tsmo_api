package com.mx.tsmo.cobertura.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CoberturaPost {

    private String cpOrigen;
    private String cpDestino;

}
