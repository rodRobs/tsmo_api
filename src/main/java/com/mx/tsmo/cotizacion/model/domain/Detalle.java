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
@Table(name = "DETALLES")
public class Detalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_detalles")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identificador;
    private String contenido;
    private double valorDeclarado;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_dimension")
    private Dimensiones dimensiones;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;

    /*
    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "id_cotizacion")
    private Cotizacion cotizacion;
    */
    public String toString() {
        return "Valor Declarado: " + valorDeclarado +
                "Dimensiones: " + dimensiones;
    }
}
