package com.mx.tsmo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CajaDto {

    private int largo;
    private int ancho;
    private int alto;

    private double peso;

}
