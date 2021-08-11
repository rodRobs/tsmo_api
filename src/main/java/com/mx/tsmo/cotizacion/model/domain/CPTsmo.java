package com.mx.tsmo.cotizacion.model.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "codigo_postal_tsmo")
@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class CPTsmo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int codigo;
    private String asentamiento;
    private String tipo;
    private String municipio;
    private String ciudad;
    private String estado;

}
