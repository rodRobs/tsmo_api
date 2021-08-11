package com.mx.tsmo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class DireccionDto {

    private String cp;
    private String colonia;
    private String calle;
    private String numero;
    private String delegacion;
    private String estado;

}
