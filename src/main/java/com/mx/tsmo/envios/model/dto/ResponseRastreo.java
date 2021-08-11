package com.mx.tsmo.envios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ResponseRastreo {

    private boolean isAsegurado;
    private double valorAsegurado;
    private String codigoIATAOrigen;
    private String codigoIATADestino;
    private String tipoServicio;
    private String fCaptura;
    private String estatus;
    private String ultimoMovimiento;
    private String oficinaUltimoMovimiento;
    private String fUltimoMovimiento;
    private boolean isCancelada;
    private long guiaMadreId;
    private int clienteId;
    private int cliente;
    private int tipoEnvioId;
    private String guia;
    private int tipoEstadoRastreoId;
    private String tipoEstadoRastreo;
    private boolean isValida;
    private String tipoGuia;
    private String tipoEntrega;
    private String tipoEnvio;
    private String fDocumentacion;
    private String referencia1;
    private String referencia2;
    private String remitente;
    private String paisOrigen;
    private String estadoOrigen;
    private String ciudadOrigen;
    private String coloniaOrigen;
    private String cpOrigen;
    private String calleOrigen;
    private String numeroExtOrigen;
    private String numeroIntOrigen;
    private String destinatario;
    private String destinatario2;
    private String paisDestino;
    private String estadoDestino;
    private String ciudadDestino;
    private String coloniaDestino;
    private String cpDestino;
    private String calleDestino;
    private String numeroExtDestino;
    private String numeroIntDestino;
    private String fEntrega;
    private String recibio;
    private List<Movimientos> movimientos;
    private String zona;
    private Dimensiones dimensiones;
    private EntregaDto entrega;
    private List<String> remesa;

}
