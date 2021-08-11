package com.mx.tsmo.envios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnvioBuscarDto {

    private String guia;
    private Date createAt;
    private String estadoEnvio;
    private String estadoPago;
    private String nombreCliente;
    private String origen;
    private String destino;

}

