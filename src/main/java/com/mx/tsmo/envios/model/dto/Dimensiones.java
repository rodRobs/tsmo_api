package com.mx.tsmo.envios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Dimensiones {

    private double largo;
    private double alto;
    private double ancho;
    private double peso;
    private double volumen;

}
