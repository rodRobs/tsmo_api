package com.mx.tsmo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CotizacionDto {

    // Origen
    private String cpOrigen;
    private String coloniaOrigen;
    private String calleOrigen;
    private String numeroOrigen;
    private String delegacionOrigen;
    private String ciudadOrigen;
    private String estadoOrigen;
    private String paisOrigen;
    // Destino
    private String cpDestino;
    private String coloniaDestino;
    private String calleDestino;
    private String numeroDestino;
    private String delegacionDestino;
    private String ciudadDestino;
    private String estadoDestino;
    private String paisDestino;
    // Tamanio
    private int largo;
    private int ancho;
    private int alto;
    // Peso
    private double peso;
    // Valor
    private double valor;
    // Contenido
    private String contenido;
    // Referencia
    private String referencia;

    @Override
    public String toString() {
        return
        "cpOrigen: " + cpOrigen +
        "coloniaOrigen: " + coloniaOrigen +
        "calleOrigen: " + calleOrigen +
        "numeroOrigen: " + numeroOrigen +
        "delegacionOrigen: " + delegacionOrigen +
        "estadoOrigen: " + estadoOrigen +
        // Destino
        "cpDestino: " + cpDestino +
        "coloniaDestino: " + coloniaDestino +
        "calleDestino: " + calleDestino +
        "numeroDestino: " + numeroDestino +
        "delegacionDestino: " + delegacionDestino +
        "estdaoDestino: " + estadoDestino +
        // Tamanio
        "largo: " + largo +
        "ancho: " + ancho +
        "alto:" + alto +
        // Peso
        "peso: " + peso +
        "valor: " + valor;
    }

}
