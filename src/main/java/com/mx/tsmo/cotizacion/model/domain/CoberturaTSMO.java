package com.mx.tsmo.cotizacion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cobertura_tsmo")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CoberturaTSMO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String zona;
    private String asentamiento;
    private String municipio;
    private String ciudad;
    private String estado;

}
