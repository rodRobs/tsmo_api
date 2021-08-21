package com.mx.tsmo.documentacion.model.domain;

import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import com.mx.tsmo.cotizacion.model.domain.Servicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "DOCUMENTACIONES")
public class Documentacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documentacion")
    private Long id;
    private String referencia1;
    private String referencia2;
    private String contiene;
    /*@OneToOne
    @JoinColumn(name = "id_servicios")
    private Servicio servicios;*/
    @OneToOne
    @JoinColumn(name = "id_cotizacion")
    private Cotizacion cotizacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false)
    private Date createAt;
}
