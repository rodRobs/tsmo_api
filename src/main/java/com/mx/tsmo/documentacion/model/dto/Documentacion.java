package com.mx.tsmo.documentacion.model.dto;

import com.mx.tsmo.cotizacion.model.dto.DetalleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Documentacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cuenta;
    private Opciones opciones;
    private String referencia1;
    private String referencia2;
    private String contiene;
    private Origen origen;
    private Destino destino;
    private List<DetalleDto> detalle;
    private List<Servicios> servicios;

    public String toString() {
        return "Cuenta: " + cuenta +
                " Opciones: " + opciones.toString() +
                " Referencia1: " + referencia1 +
                " Referencia2: " + referencia2 +
                " Contiene: " + contiene +
                " Origen: " + origen.toString() +
                " Destino: " + destino.toString() +
                " Detalle: " + detalle.toString();

    }

}
