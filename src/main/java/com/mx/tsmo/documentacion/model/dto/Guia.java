package com.mx.tsmo.documentacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guia implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guia;
}
