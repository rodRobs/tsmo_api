package com.mx.tsmo.envios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Movimientos {

    private String fMovimiento;
    private String movimientoId;
    private String codigo;
    private String movimiento;
    private String oficina;

}
