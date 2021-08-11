package com.mx.tsmo.cotizacion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "OPCIONES")
public class Opciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opciones")
    private Long id;
    @Column(length = 1)
    private String tipoEnvio;
    @Column(length = 1)
    private String tipoEntrega;
    @Column(length = 10)
    private String tipoServicio;
    @Column(length = 1)
    private String tipoCobro;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;

}
