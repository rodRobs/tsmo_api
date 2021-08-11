package com.mx.tsmo.envios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class EntregaDto {

    private String fEntrega;
    private String recibio;
    private String tipoIdentificacion;
    private String folioIdentificacion;
    private double latitud;
    private double longitud;
    private String firma;
    private String foto;
    private List<String> adicionales;

}
