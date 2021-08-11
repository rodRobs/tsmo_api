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
@Table(name = "DOMICILIOS")
public class Domicilio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_domicilio")
    private Long id;
    @Column(length = 60)
    private String pais;
    @Column(length = 60)
    private String estado;
    @Column(length = 60)
    private String ciudad;
    @Column(length = 60)
    private String colonia;
    @Column(name = "codigo_postal", length = 8)
    private String codigoPostal;
    @Column(length = 200)
    private String calle;
    @Column(name = "numero_interior", length = 8)
    private String numeroInt;
    @Column(name = "numero_exterior", length = 8)
    private String numeroExt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;
}
