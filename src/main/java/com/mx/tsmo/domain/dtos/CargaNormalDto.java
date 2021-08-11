package com.mx.tsmo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CargaNormalDto {

    private Long id;
    private double carga;
    private Float flete;
    private Float recoleccion;
    private Float domicilio;
    private Float cxc;
    private Float precio;

}
