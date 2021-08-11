package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CargaDto {

    private Long id;
    private double carga;
    private Float flete;
    private Float recoleccion;
    private Float domicilio;
    private Float cxc;
    private Float precio;
    private double utilidad;
    private Float peso;

}
