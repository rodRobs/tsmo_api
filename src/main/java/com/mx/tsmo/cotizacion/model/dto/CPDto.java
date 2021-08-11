package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CPDto {

    private String cp;
    private List<String> asentamiento;
    private String tipo_asentamiento;
    private String municipio;
    private String estado;
    private String ciudad;
    private String pais;

}
