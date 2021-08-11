package com.mx.tsmo.envios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Referencia {

    private int cuenta;
    private String referencia1;
    private String referencia2;
}
