package com.mx.tsmo.envios.model.dto;

import lombok.Data;

@Data
public class CiudadesDto {

    private String origen;
    private String destino;
    private String busqueda;
    private Boolean params;

}
