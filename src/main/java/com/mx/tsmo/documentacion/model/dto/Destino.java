package com.mx.tsmo.documentacion.model.dto;

import com.mx.tsmo.cotizacion.model.dto.DomicilioDto;
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
public class Destino implements Serializable {

    private static final long serialVersionUID = 1L;

    private String destinatario;
    private String destinatario2;
    private DomicilioDto domicilio;
    private List<Telefonos> telefonos;
    private String email;
    private String referencia;
    //private Detalle detalle;

}
