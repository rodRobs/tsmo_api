package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class PostCotizacionDto {

    private String Cuenta;
    private OpcionesDto Opciones;
    private OrigenDto Origen;
    private DestinoDto Destino;
    private List<DetalleDto> Detalle;

}
