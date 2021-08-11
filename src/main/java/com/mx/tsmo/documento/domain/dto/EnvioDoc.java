package com.mx.tsmo.documento.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class EnvioDoc {

    private String nombre;
    private String fecha;
    private String guia;
    private String remitente;
    private String calle_origen;
    private String numero_exterior_origen;
    private String numero_interior_origen;
    private String colonia_origen;
    private String codigo_postal_origen;
    private String ciudad_origen;
    private String estado_origen;
    private String pais_origen;
    private String numero_telefono_origen;
    private String email_origen;
    private String destinatario_destino;
    private String destinatario_destino2;
    private String calle_destino;
    private String numero_exterior_destino;
    private String numero_interior_destino;
    private String colonia_destino;
    private String codigo_postal_destino;
    private String ciudad_destino;
    private String estado_destino;
    private String pais_destino;
    private String telefono_destino;
    private String email_destino;
    private String largo;
    private String ancho;
    private String alto;
    private String peso;
    private String tipo_entrega;
    private String tipo_envio;
    private String costo_total;

}
