package com.mx.tsmo.cotizacion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "ORIGENES")
public class Origen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_origen")
    private long id;
    @Column(length = 100)
    private String remitente;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_domicilio")
    private Domicilio domicilio;
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_origen")
    private List<Telefono> telefonos;
    private String email;
    private String referencia;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;
}
