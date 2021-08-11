package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrigenDto {

    private DomicilioDto Domicilio;
}
