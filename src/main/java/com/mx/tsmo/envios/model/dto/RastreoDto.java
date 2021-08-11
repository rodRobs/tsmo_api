package com.mx.tsmo.envios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RastreoDto {

    private int etapa;
    private String nombre;
    private String descripcion;
    private ArrayList<String> guias;
    private String municipio;
    private String estado;
    private String pais;
    private String latitud;
    private String longitud;



}
