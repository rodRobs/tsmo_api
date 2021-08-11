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
@Table(name = "DIMENSIONES")
public class Dimensiones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dimension")
    private Long id;
    private short largo;
    private short alto;
    private short ancho;
    private short peso;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;

}
