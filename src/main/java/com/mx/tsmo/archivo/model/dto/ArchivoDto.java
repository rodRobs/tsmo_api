package com.mx.tsmo.archivo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ArchivoDto {

    private String destinatario;
    private String calle;
    private String numeroExt;
    private String numeroInt;
    private String colonia;
    private String cp;
    private String ciudad;
    private String estado;
    private String pais;
    private String telefono;
    private String correo;
    private String pedido;
    private String cantidad;

}
